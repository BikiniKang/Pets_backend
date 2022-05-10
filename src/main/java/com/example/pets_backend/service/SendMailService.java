package com.example.pets_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

@Service
public class SendMailService {

    @Value("bikinikang@gmail.com")
    private String receiver;

    private JavaMailSenderImpl mailSender;

    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail() {
        Properties mailProperties = mailSender.getJavaMailProperties();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailSender.getUsername());
        msg.setTo( receiver.split(","));
        msg.setText("Have a nice day !");
        msg.setSubject("Test Send Mail");
        mailSender.send(msg);
    }

//    public void sendMailAttach(String filePath) throws Exception {
//        Properties mailProperties = mailSender.getJavaMailProperties();
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper( message, true );
//        helper.setFrom(mailSender.getUsername());
//        helper.setTo( receiver.split("," ));
//        helper.setText("Have a nice day !", true);
//        helper.setSubject("Test Send Mail with File");
//
//        FileSystemResource file = new FileSystemResource(new File(filePath));
//        helper.addAttachment("required_data_20210824.csv", file);
//
//        mailSender.send(message);
//    }
}
