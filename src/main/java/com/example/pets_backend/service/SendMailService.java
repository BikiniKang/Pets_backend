package com.example.pets_backend.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.TEAM_EMAIL;

@Service
@Slf4j
public class SendMailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

//    public void sendMail(String typeStr, String receiver, String receiverName, String content) {
////        Properties mailProperties = mailSender.getJavaMailProperties();
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(TEAM_EMAIL);
//        msg.setTo(receiver);
//        String mailText = "Hi " + receiverName + ", \n\n" +
//                "You have " + typeStr + " starts in 1 hour: \n" +
//                "        " + content;
//        msg.setText(mailText);
//        msg.setSubject("Pet Pocket Reminder");
//        mailSender.send(msg);
//    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(TEAM_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
//        helper.addInline("attachment.png", new ClassPathResource("images/app-logo"));
        mailSender.send(message);
        log.info("Sent email to '{}' at {}", to, LocalDateTime.now());
    }

    public void sentEmailForEvent(String to, String subject, Map<String, Object> templateModel) throws Exception {
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("template-freemarker.ftlh");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        sendHtmlEmail(to, subject, htmlBody);
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
