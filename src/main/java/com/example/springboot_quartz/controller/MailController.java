package com.example.springboot_quartz.controller;

import com.example.springboot_quartz.model.po.MailPo;
import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import com.example.springboot_quartz.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hewei on 2018/10/8.
 */
@RestController
@RequestMapping("/mail")
@Api("邮件发送接口")
public class MailController {

    @Autowired
    MailService mailService;

    @GetMapping("/send")
    @ApiOperation("springboot——javaMailSender发送简单邮件")
    public String sendMail(){
        mailService.sendSimpleMail("测试主题","测试内容");
        return "发送成功";
    }

    @GetMapping("/sendHtml")
    @ApiOperation("springboot——javaMailSender发送html邮件")
    public String sendHtmlMail(){
        String content= "<html>\n" +
                "<body>\n" +
                "<h3>hello world!这是一封html邮件</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail("测试html邮件",content);
        return "发送成功";
    }

    @GetMapping("/sendAttachment")
    @ApiOperation("springboot——javaMailSender发送带附件的邮件")
    public String sendAttachmentMail(){
        String filePath = "D:\\aa.csv";
        String content= "<html>\n" +
                "<body>\n" +
                "<h3>hello world!这是一封html邮件</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendAttachmentsMail("测试附件主题",content,filePath);
        return "发送成功";
    }

    @GetMapping("/sendInlineResourceMail")
    @ApiOperation("springboot——javaMailSender发送带有静态资源的邮件")
    public String sendInlineResourceMail(){
        String rscId="pic";
        String content= "<html>\n" +
                "<body>\n" +
                "<h3>hello world!这是一封html邮件:<img src=\'cid:"+rscId+"\'></h3>\n" +
                "</body>\n" +
                "</html>";
        String imgPath = "D:\\workspace\\TestNGProj\\base-output\\index.html";
        mailService.sendInlineResourceMail("测试带有静态资源的邮件","邮件内容",imgPath,rscId);
        return "发送成功";
    }

    @PostMapping("sendSimple")
    @ApiOperation("javaMailApi发送简单邮件")
    public Result sendSimpleMail(@RequestBody @Validated MailPo mailPo){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        try {
            mailService.sendMailByJavaMail(mailPo.getSubject(),mailPo.getContent());
        } catch (Exception e) {
            result.setMsg(RESULT_STATUS.SERVER_UNKONW_ERROR.msg);
            result.setCode(RESULT_STATUS.SERVER_UNKONW_ERROR.code);
            return result;
        }
        return result;
    }

    @PostMapping("sendAttachmentsByJavaMail")
    @ApiOperation("javaMailApi发送带有附件的邮件")
    public Result sendAttachmentsByJavaMail(@RequestBody @Validated MailPo mailPo){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        try {
            mailService.sendAttachmentsByJavaMail(mailPo.getSubject(),mailPo.getContent());
        } catch (Exception e) {
            result.setMsg(RESULT_STATUS.SERVER_UNKONW_ERROR.msg);
            result.setCode(RESULT_STATUS.SERVER_UNKONW_ERROR.code);
            return result;
        }
        return result;
    }
}
