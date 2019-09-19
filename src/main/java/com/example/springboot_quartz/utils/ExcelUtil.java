package com.example.springboot_quartz.utils;

import com.example.springboot_quartz.model.case_po.Case;
import com.example.springboot_quartz.model.case_po.Rest;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sun.rmi.runtime.Log;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Map;

import static com.example.springboot_quartz.service.FileUploadService.getCellValue;
import static com.example.springboot_quartz.utils.CaseUtil.fieldNameAndColumNumMap;

/**
 * created by ${user} on 2019/9/16
 */
@Slf4j
public class ExcelUtil {

    public static <T> void loadBeans(String filepath,Integer sheetNum,Class<T> clazz){
        try (InputStream iStream = new FileInputStream(new File(filepath))){
            Workbook workbook = WorkbookFactory.create(iStream);
            Sheet sheet = workbook.getSheetAt(sheetNum);
            int lastRowNum = sheet.getLastRowNum();
            String[] fields = null;
            //遍历excel每一行数据
            for (int i = 0; i < lastRowNum+1; i++) {
                Row row = sheet.getRow(i);
                int lastCellNum = row.getLastCellNum();
                if (i == 0){
                    fields = new String[lastCellNum];
                    //遍历表头
                    for (int j = 0; j < lastCellNum; j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String cellValue = getCellValue(cell);
                        //截取（前面的值
                        String cellNew = cellValue.substring(0, cellValue.indexOf("("));
                        fields[j] = cellNew;
                        //获取列的索引
                        int column = cell.getAddress().getColumn();
                        fieldNameAndColumNumMap.put(cellNew,column);
                    }
                }else {
                    Object object = clazz.newInstance();
                    //遍历单元格，并且通过反射设置
                    for (int j = 0; j < lastCellNum; j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String methodName = "set"+fields[j];
                        Method method = clazz.getMethod(methodName, String.class);
                        method.invoke(object,getCellValue(cell));
                    }
                    if (object instanceof Rest){
                        Rest rest = (Rest) object;
                        RestUtil.restList.add(rest);
                    }
                    if (object instanceof Case){
                        Case aCase = (Case) object;
                        CaseUtil.caseList.add(aCase);
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }


    public static void writeBatchToExcel(String filePath, Integer sheetNum){
        FileOutputStream outputStream = null;
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(sheetNum - 1);
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                if (i > 0){
                    Row row = sheet.getRow(i);
                    Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    for (Map.Entry<String, Map<String, String>> entry : CaseResultUtil.caseIdToCellNameToResultMap.entrySet()) {
                        if (entry.getKey().equals(cell.getStringCellValue())){
                            /**
                             * cellNam-result map
                             *ActualResponseData-result
                             * PreValidateResult-result
                             * AfterValidateResult-result
                             */
                            Map<String, String> cellNameToResultMap = entry.getValue();
                            for (Map.Entry<String, String> stringEntry : cellNameToResultMap.entrySet()) {
                                Integer cellIndex = CaseUtil.fieldNameAndColumNumMap.get(stringEntry.getKey());
                                String result = stringEntry.getValue();
                                Cell resultCell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                                resultCell.setCellType(CellType.STRING);
                                if (result.length()>=32767) {
                                    result = result.substring(0,300);
                                }
                                resultCell.setCellValue(result);
                            }
                            //如果该行找到了对应的case，则跳出当前case循环
                            break;
                        }
                    }
                }
            }
             outputStream = new FileOutputStream(new File(filePath));
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeContentToExcel(Row row){
        int lastCellNum = row.getLastCellNum();
        for (int j = 0; j < lastCellNum; j++) {
            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            //遍历所有case结果，循环写入cell
            for (Map.Entry<String, Map<String, String>> entry : CaseResultUtil.caseIdToCellNameToResultMap.entrySet()) {
                String caseId = entry.getKey();
                Map<String, String> cellNameToResultMap = entry.getValue();
                if (caseId.equals(getCellValue(cell))){
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("");
                    for (Map.Entry<String, String> stringEntry : cellNameToResultMap.entrySet()) {
                        Integer collumIndex = CaseUtil.fieldNameAndColumNumMap.get(stringEntry.getKey());


                    }
                }

            }
        }
    }

}
