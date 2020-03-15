package com.example.springboot_quartz.controller;

import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import com.example.springboot_quartz.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

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
            if (Objects.isNull(workbook)){
                return new Result().success(RESULT_STATUS.SUCCESS.code,"文件不能为空");
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                workbook.write(outputStream);
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + "测试文件");
//设置文件大小
                response.setContentLength(outputStream.size());
//创建Cookie并添加到response中
//                Cookie cookie = new Cookie("fileDownload", "true");
//                cookie.setPath("/");
//                response.addCookie(cookie);

                outputStream.writeTo(response.getOutputStream());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            result.setData("上传失败");
//            return result;
//            byte[] bytes = outByteStream.toByteArray();
//            result.setData(bytes);
        }
        return null;
    }



}
