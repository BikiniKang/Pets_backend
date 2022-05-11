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

import static com.example.pets_backend.ConstantValues.TEAM_EMAIL;

@Service
public class SendMailService implements Runnable{

//    @Value("bikinikang@gmail.com")
//    private String receiver;

    private JavaMailSenderImpl mailSender;


    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String typeStr, String receiver, String receiverName, String content) {
//        Properties mailProperties = mailSender.getJavaMailProperties();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(TEAM_EMAIL);
        msg.setTo(receiver);
        String mailText = "Hi " + receiverName + ", \n" +
                "You have " + typeStr + " starts in 1 hour: \n" +
                "        " + content;
        msg.setText(mailText);
        msg.setSubject("Pet Pocket Reminder");
        mailSender.send(msg);
    }

    @Override
    public void run() {

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
