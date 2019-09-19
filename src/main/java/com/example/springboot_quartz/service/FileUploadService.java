package com.example.springboot_quartz.service;

import com.example.springboot_quartz.config.ExcelUtil;
import com.example.springboot_quartz.enums.ExcelTitleEnum;
import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.io.File.separator;

/**
 * created by ${user} on 2019/7/26
 */
@Service
@Slf4j
public class FileUploadService {

    /**
     * ServletFileUpload 文件上传
     * @param request
     * @return
     */
    public Result uploadFile(HttpServletRequest request){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        if (!ServletFileUpload.isMultipartContent(request)){
            result.setMsg("没有文件上传");
            result.setCode(RESULT_STATUS.PARAM_ERROR.code);
            return result;
        }
        try {
            List<DiskFileItem> list = fileUpload.parseRequest(request);
            for (DiskFileItem fileItem : list) {
                System.out.println(fileItem);
            }
            for (DiskFileItem fileItem : list) {
                if (fileItem.isFormField()){
                    log.info("表单值：{}",fileItem.getFieldName());
                }else {
                    String originName = fileItem.getName();
                    if (StringUtils.isEmpty(originName)){
                        log.info("文件名为空");
                        continue;
                    }
                    String suffixName = originName.substring(originName.lastIndexOf("."));
                    String prefixName = UUID.randomUUID().toString();
                    String fileName = prefixName+suffixName;
                    String newFilePath = System.getProperty("user.dir")+ separator+"upload"+ separator+fileName;
//                    File file = new File(fileName);
//                    fileItem.write(file);
                    InputStream inputStream = fileItem.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(newFilePath);
                    byte buffer[] = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer))>0){
                        outputStream.write(buffer,0,len);
                    }
                    inputStream.close();
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * MultipartFile文件上传
     * @param request
     * @return
     */
    public String uploadFileByMultipartFile(HttpServletRequest request){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        String filePath = "";
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (!commonsMultipartResolver.isMultipart(request)){
            result.setCode(RESULT_STATUS.PARAM_ERROR.code);
            result.setMsg("不是Multipart提交");
        }else {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
            Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
            while (fileNames.hasNext()){
                String fileName = fileNames.next();
                if (StringUtils.isEmpty(fileName)){
                    log.info("文件名为空");
                    continue;
                }
                MultipartFile multipartFile = multipartHttpServletRequest.getFile(fileName);
                String originalFilename = multipartFile.getOriginalFilename();
                String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
                filePath = System.getProperty("user.dir")+separator+"upload"+ separator+newFileName;
                File file = new File(filePath);
                try {
                    multipartFile.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    /**
     * 解析excel
     * @param request
     * @return
     */
    public Map<String,Object> parseExcelAndUpload(HttpServletRequest request){
        Map<String,Object> map = Maps.newHashMap();
        StringBuffer buffer = new StringBuffer();
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (!resolver.isMultipart(request)){
            log.info("上传格式错误");
            buffer.append("上传格式错误");
            map.put("buffer",buffer.toString());
        }else {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
            while (fileNames.hasNext()){
                MultipartFile multipartFile = multipartHttpServletRequest.getFile(fileNames.next());
                String originalFilename = multipartFile.getOriginalFilename();
                if (!originalFilename.endsWith(".xls") && !originalFilename.endsWith(".xlsx")){
                    log.info("上传的文件必须是xls或者xlsx格式");
                    map.put("buffer",buffer.append(buffer.append("上传的文件必须是xls或者xlsx格式")).toString());
                }else {
                    Workbook workbook = null;
                    try(InputStream inputStream = multipartFile.getInputStream()) {
                        workbook = WorkbookFactory.create(inputStream);
                        //检查excel内容
                        checkUploadFileProperties(workbook,map);
                    }catch (Exception e){
                        map.put("buffer","文件解析异常");
                        map.put("workbook",workbook);
                        return map;
                    }
                    if (Objects.isNull(map.get("buffer"))){
                        String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
                        String filePath = System.getProperty("user.dir")+separator+"upload"+ separator+newFileName;
                        File file = new File(filePath);
                        try {
                            multipartFile.transferTo(file);
                        } catch (IOException e) {
                            map.put("buffer","文件上传异常");
                            return map;
                        }
                    }
                }
//                else {
//                    try {
//                        InputStream inputStream = multipartFile.getInputStream();
//                        if (originalFilename.endsWith(".xls")){
//                            excelList = ExcelUtil.readExcelXls(inputStream);
//                            map.put("excelList",excelList);
//                        }else if (originalFilename.endsWith(".xlsx")){
//                            excelList = ExcelUtil.readExcel(inputStream);
//                            map.put("excelList",excelList);
//                        }
//                    } catch (Exception e) {
//                        log.error("错误");
//                        map.put("errorMsg","程序错误");
//                    }
//
//                }
            }
        }
        return map;
    }

    private Map<String,Object> checkUploadFileProperties(Workbook workbook,Map<String,Object> map) {
        StringBuffer errorMsgBuffer = new StringBuffer();
        Sheet sheet = workbook.getSheetAt(0);
        // 获取最后一行, 计数从0开始
        int lastRowNum = sheet.getLastRowNum();

        // 校验最大行数
        if (lastRowNum > 10000) {
            String reason = "文件最大只能支持" + 10000 + "条,请重新上传";
            errorMsgBuffer.append(reason);
            map.put("buffer",errorMsgBuffer.toString());
            return map;
        }

        // 校验最小行数
        if (lastRowNum < 1) {
            String reason = "文件不能为空,请重新上传";
            errorMsgBuffer.append(reason);
            map.put("buffer",errorMsgBuffer.toString());
            return map;
        }

        // 校验表头
        Row row = sheet.getRow(0);
        List cellList = Lists.newArrayList();
        for (Cell cell : row) {
            cellList.add(cell.getStringCellValue());
        }
        boolean flag = checkTitle(cellList, ExcelTitleEnum.getTitleNameList());
        if (!flag){
            errorMsgBuffer.append("上传的文件和模板不匹配");
            map.put("buffer",errorMsgBuffer.toString());
            return map;
        }
        //检查excel内容
        checkUploadFileContent(sheet,errorMsgBuffer);
        map.put("workbook",workbook);
        map.put("buffer",errorMsgBuffer);
        return map;
    }


    private void checkUploadFileContent(Sheet sheet,StringBuffer buffer){
        Short lastCellNum = sheet.getRow(0).getLastCellNum();
        Cell firstHeadCell = sheet.getRow(0).createCell(lastCellNum);
        firstHeadCell.setCellValue("错误原因");
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            checkCell(row,buffer);
        }
    }


    /**
     * 检查excel数据
     * @param
     * @return
     */
//    public Map<String,Object> checkErrorMsg(List<List<List<String>>> excelList){
//        Map<String,Object> map = Maps.newHashMap();
//        if (excelList.size()>1){
//            map.put("errorMsg","不能上传多个sheet");
//            return map;
//        }
//        List<List<String>> rowList = excelList.get(0);
//        if (CollectionUtils.isEmpty(rowList) || rowList.size() < 2){
//            map.put("errorMsg","文件不能为空");
//            return map;
//        }
//        List<String> cellList = rowList.get(0);
//        List<String> copyList = new ArrayList<>(cellList);
//        for (int i = 0; i < copyList.size(); i++) {
//            if (StringUtils.isEmpty(copyList.get(i))){
//                cellList.remove(i);
//            }
//        }
//        boolean flag = checkTitle(cellList, ExcelTitleEnum.getTitleNameList());
//        if (!flag){
//            map.put("errorMsg","上传的文件和模板不匹配");
//            return map;
//        }
//        checkRowList(rowList,ExcelTitleEnum.getTitleNameList());
//        return null;
//    }

    private boolean checkTitle(List<String> titleList, List<String> titleNameList){
        if (titleList.size() != titleNameList.size()){
            return false;
        }
        for (int i = 0; i < titleList.size(); i++) {
            if (!org.apache.commons.lang.StringUtils.equals(titleList.get(i),titleNameList.get(i))){
                return false;
            }
        }
        return true;

    }


    private void checkCell(Row row,StringBuffer buffer){
        Integer cellCount = Integer.valueOf(row.getLastCellNum());
        for (int i = 0;i<cellCount;i++){
            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            switch (i){
                case 0:
                    String value = getCellValue(cell);
                    if (!NumberUtils.isDigits(value)){
                        buffer.append("id必须为数字");
                        row.createCell(cellCount).setCellValue(buffer.toString());
                    }
                    break;
                case 1:
                    if (StringUtils.isEmpty(getCellValue(cell))){
                        buffer.append("客户名称不能为空");
                        row.createCell(cellCount).setCellValue(buffer.toString());
                    }
                    break;
                case 2:
                    if (!NumberUtils.isDigits(getCellValue(cell))){
                        buffer.append("客户年龄必须为数字");
                        row.createCell(cellCount).setCellValue(buffer.toString());
                    }
                    break;
                case 3:
                    if (StringUtils.isEmpty(getCellValue(cell))){
                        buffer.append("客户性别不能为空");
                        row.createCell(cellCount).setCellValue(buffer.toString());
                    }
                    break;
                case 4:
                    if (StringUtils.isEmpty(getCellValue(cell))){
                        buffer.append("班级不能为空");
                        row.createCell(cellCount).setCellValue(buffer.toString());
                    }
                    break;
                case 5:
                    if (StringUtils.isEmpty(getCellValue(cell))){
                        buffer.append("地址不能为空");
                        row.createCell(cellCount).setCellValue(buffer.toString());
                    }
                    break;
                case 6:
                    if (StringUtils.isEmpty(getCellValue(cell))){
                        buffer.append("电话不能为空");
                        row.createCell(cellCount).setCellValue(buffer.toString());
                    }
                    break;
            }
        }
    }


    public static String getCellValue(Cell cell) {
        String value = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    value = null;
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    Workbook wb = cell.getSheet().getWorkbook();
                    CreationHelper crateHelper = wb.getCreationHelper();
                    FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                    value = getCellValue(evaluator.evaluateInCell(cell));
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        Date theDate = cell.getDateCellValue();
                        value = new SimpleDateFormat("yyyy-dd-mm").format(theDate);
                    } else {
                        value = NumberToTextConverter.toText(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString();
                    break;
                default:
                    value = null;
            }
        }
        return value;
    }
    public static void main(String[] args) {
        File file = new File(System.getProperty("user.dir")+separator+"upload"+ separator+"file.xlsx");
        if (!file.exists()){
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(file.getPath());
    }

}
