package com.example.pets_backend.service;

import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.TEAM_EMAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMailService {

    private final JavaMailSenderImpl mailSender;
    private final FreeMarkerConfigurer freemarkerConfigurer;

    public void sendEmail(String to, Map<String, String> templateModel, String templateName) throws Exception {
        log.info("Sending email to '{}'", to);
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate(templateName);
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        sendHtmlEmail(to, htmlBody);
    }

    private void sendHtmlEmail(String to, String htmlBody) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(TEAM_EMAIL);
        helper.setTo(to);
        helper.setSubject("Pet Pocket Reminder");
        helper.setText(htmlBody, true);
        mailSender.send(message);
        log.info("Email has been sent to '{}'", to);
    }

}
