package com.example.springboot_quartz.controller;

import com.example.springboot_quartz.utils.ZipUtils;
import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import com.example.springboot_quartz.service.FileUploadService;
import com.google.common.collect.Lists;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * created by ${user} on 2019/7/29
 */
//@RestController
@RequestMapping("/file")
@Controller
@Slf4j
@Api(value = "文件上传接口",position = 2)
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;


    @PostMapping("/upload")
    @ResponseBody
    @ApiOperation(value = "上传文件-ServletFileUpload")
    public Result uploadFileByServletFileUpload(@RequestParam(value = "username") String username,@RequestParam(value = "password") String password,HttpServletRequest request){
        log.info("username:{}",username);
        log.info("password:{}",password);
        Result result = fileUploadService.uploadFile(request);
        return result;
    }

    @GetMapping("/html")
    public String html() {
        return "/page.html";
    }

    @PostMapping("/uploadFileByMultipartFile")
    @ResponseBody
    @ApiOperation(value = "上传文件-MultipartFile")
    public Result uploadFileByMultipartFile(HttpServletRequest re){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        String filepath = fileUploadService.uploadFileByMultipartFile(re);
        result.setData(filepath);
        return result;
    }

    @PostMapping("uploadExcel")
    @ResponseBody
    @ApiOperation(value = "上传excel-parseExcel")
    public Result uploadExcel(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        //对文件
        Map<String, Object> map = fileUploadService.parseExcelAndUpload(request);
        if (!StringUtils.isEmpty(map.get("buffer"))){
            Workbook workbook=(Workbook) map.get("workbook");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                workbook.write(outputStream);
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + "测试文件");
                outputStream.writeTo(response.getOutputStream());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return result;
    }


    /**
     * 上传excel，并打包下载错误文件
     * @param request
     * @param response
     * @return
     */
    @PostMapping("uploadExcelAndDownZip")
    @ResponseBody
    @ApiOperation(value = "上传excel-parseExcel")
    public Result uploadExcelAndDown(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        //对文件
        Map<String, Object> map = fileUploadService.parseExcelAndUpload(request);
        if (!StringUtils.isEmpty(map.get("buffer"))){
            Workbook workbook=(Workbook) map.get("workbook");
            File error = new File("D:\\test\\error.xls");
            File zipFile = new File("D:\\test\\error.zip");
            try (FileOutputStream fos = new FileOutputStream(error)){
                workbook.write(fos);
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
                ZipUtils.compress(error,zos);
                zos.close();
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + zipFile.getName());
                try(FileInputStream fis = new FileInputStream(zipFile)){
                    byte[] buffer = new byte[1024];
                    BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                    while (fis.read(buffer)!=-1){
                        bos.write(buffer);
                    }
                    bos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (error!=null){
                error.delete();
            }
            if (zipFile!=null){
                zipFile.delete();
            }
            return null;
        }
        return result;
    }

    @RequestMapping("/down")
    @ResponseBody
    public void downZip(HttpServletResponse response) throws IOException {
        File file = null;
        try {
            file = new File("D:\\test2\\a.zip");
            ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(file));
            List<File> fileList = Lists.newArrayList();
//            fileList.add(new File("D:\\test\\test.txt"));
//            fileList.add(new File("D:\\test\\hello.xls"));
//            fileList.add(new File("D:\\test\\jdk8.CHM"));
            fileList.add(new File("D:\\test\\"));
            for (File file1 : fileList) {
                ZipUtils.compress(file1,zo);
            }
            zo.flush();
            zo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        File sourceFile = file;
        downloadFileShowPercent(response,sourceFile,sourceFile.getName());
//        String zipPath = "D:\\test\\new.zip";
//        File file = new File(zipPath);
//        FileInputStream fi = new FileInputStream(sourceFile);
//
//        response.setContentType("application/zip");
//        response.setHeader("content-disposition","attachment;filename="+new String(sourceFile.getName().getBytes(),"ISO8859-1"));
//
//
////        OutputStream os = response.getOutputStream();
//
//        ZipOutputStream zo = new ZipOutputStream(response.getOutputStream());
//        zo.putNextEntry(new ZipEntry(sourceFile.getName()));
//        byte[] data = new byte[1024];
//        while (fi.read(data)!=-1){
//            zo.write(data);
//        }
//        zo.flush();
//        zo.closeEntry();
       /* byte[] buffer = new byte[1024];
        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file));
        while (bi.read(buffer)!=-1){
            os.write(buffer);
        }
        os.flush();
        if (os!=null){
            os.close();
        }*/
//        if (bi!=null){
//            bi.close();
//        }

//        if (zo!=null){
//            zo.close();
//        }
//        if (fi!=null){
//            fi.close();
//        }
    }



    private void downloadFileShowPercent(HttpServletResponse response, File file,
                                         String fileName) {
        BufferedOutputStream bw = null;
        FileInputStream fis = null;
        try {
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/octet-stream");
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            response.addHeader("content-disposition", "attachment;filename=" + fileName);
            fis = new FileInputStream(file);
            bw = new BufferedOutputStream(out);
            byte[] chars = new byte[1024 * 8];
            int index = -1;
            while ((index = fis.read(chars)) != -1) {
                bw.write(chars, 0, index);
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            log.error("downloadFileShowPercent method error:" + e.getMessage(), e);
            //下载文件的过程中出现异常，返回-1.0 控制 实时刷新方法showDownLoadPercent停止轮询请求
//            vo.setPercent(-1.0);
//            vo.setCount(-1);
//            getMap().put(sessionUserBean.getSid(), vo);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("close error!", e);
                }
            }
//            if (file != null) {
//                file.delete();
//            }
        }
    }
}
