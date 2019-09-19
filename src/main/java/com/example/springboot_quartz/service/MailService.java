package com.example.springboot_quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

/**
 * Created by hewei on 2018/10/8.
 */
@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.to}")
    private String to;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.password}")
    private String password;


    /**
     * 发送一个简单邮件
     * @param subject 主题
     * @param text 内容
     */
    public void sendSimpleMail(String subject,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("发送邮件异常",e);
        }
    }

    /**
     *和文本邮件发送代码对比，富文本邮件发送使用 MimeMessageHelper 类。MimeMessageHelper 支持发送复杂邮件模板，支持文本、附件、HTML、图片等
     * @param subject 主题
     * @param text 内容
     */
    public void sendHtmlMail(String subject,String text){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);
            javaMailSender.send(message);
            log.info("html邮件发送成功");
        } catch (MessagingException e) {
            log.error("发送html邮件发生异常",e);
        }
    }

    /**
     * 发送带附件
     * @param subject
     * @param text
     * @param filePath
     */
    public void sendAttachmentsMail(String subject,String text,String filePath){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String filename = file.getFilename();
            helper.addAttachment(filename,file);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.info("发送带附件的邮件时发生异常",e);
        }
    }

    /**
     * 发送带静态资源的邮件
     * 添加多个图片可以使用多条 <img src='cid:" + rscId + "' > 和helper.addInline(rscId, res) 来实现
     * @param subject
     * @param text
     * @param rscPath
     * @param rscId
     */
    public void sendInlineResourceMail(String subject,String text,String rscPath,String rscId){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource resource = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId,resource);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送嵌入静态资源的邮件发送异常",e);
        }
    }

    /**
     * 使用javaMailApi发送简单邮件
     * @param subject
     * @param text
     */
    public void sendMailByJavaMail(String subject,String text){
//        MimeMessage
        Session session = getSession();
        MimeMessage mimeMessage = new MimeMessage(session);
        //设置收件人地址、发件人地址，主题以及邮件内容
        try {
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipients(Message.RecipientType.TO,new InternetAddress[]{new InternetAddress(to)});
//            mimeMessage.addRecipients(Message.RecipientType.CC,"wb-swh459715@alibaba-inc.com");
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(text,"text/plain;charset=utf-8");
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 使用javaMailApi发送带附件的邮件
     * @param subject
     * @param text
     */
    public void sendAttachmentsByJavaMail(String subject,String text){
//        MimeMessage
        Session session = getSession();
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMultipart  multipart = new MimeMultipart();
        //设置收件人地址、发件人地址，主题以及邮件内容
        try {
            mimeMessage.setFrom(new InternetAddress(MimeUtility.encodeText("软通人力")+"<"+from+">"));
            mimeMessage.addRecipients(Message.RecipientType.TO,new InternetAddress[]{new InternetAddress(to)});
            mimeMessage.addRecipients(Message.RecipientType.CC,new InternetAddress[]{new InternetAddress("wb-swh459715@alibaba-inc.com"),new InternetAddress("wb-lxw486803@alibaba-inc.com")});
            mimeMessage.setSubject(subject);
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.attachFile(new File("D:\\图片.jpg"));
//            mimeBodyPart.attachFile(new File("D:\\workspace\\TestNGProj\\base-output\\index.html"));
            mimeBodyPart.setFileName(MimeUtility.encodeText("这是李晓雯.jpg"));
            MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
            mimeBodyPart1.setContent(text,"text/plain;charset=utf-8");
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(mimeBodyPart1);
            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Session getSession(){
        Properties properties = new Properties();
        properties.setProperty("mail.host",host);
        properties.setProperty("mail.smtp.auth","true");
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,password);
            }
        };
        Session session = Session.getDefaultInstance(properties, authenticator);
        return session;
    }
}
