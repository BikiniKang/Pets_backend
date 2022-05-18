package com.example.pets_backend.service;

import com.example.pets_backend.entity.Event;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.TEAM_EMAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMailService {

    private final JavaMailSenderImpl mailSender;
    private final FreeMarkerConfigurer freemarkerConfigurer;


    private void sendHtmlEmail(String to, String htmlBody) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(TEAM_EMAIL);
        helper.setTo(to);
        helper.setSubject("Pet Pocket Reminder");
        helper.setText(htmlBody, true);
        mailSender.send(message);
        log.info("Sent email to '{}' at {}", to, LocalDateTime.now());
    }

    public void sendEmailForEvent(Event event) throws Exception {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("firstName", event.getUser().getFirstName());
        templateModel.put("petNames", String.join(", ", event.getPetNameList()));
        templateModel.put("eventTitle", event.getEventTitle());
        templateModel.put("eventStartTime", event.getStartDateTime());
        templateModel.put("eventLocation", "1 Anthony Rolfe Ave, Gungahlin ACT 2912"); // TODO: Event needs to have a Location attribute!!
        templateModel.put("petAvatar", "https://i.ibb.co/P6Cz8CS/image-6.png");       // TODO: use the pet's real avatar
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("template-event.ftlh");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        sendHtmlEmail(event.getUser().getEmail(),  htmlBody);
    }

    // Temporary method to support testing
    public void sendEmailForEvent(String to, Map<String, Object> templateModel) throws Exception {
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("template-event.ftlh");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        sendHtmlEmail(to, htmlBody);
    }

    public void sendEmailForTasks (String to, Map<String, String> templateModel) throws Exception {
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("template-tasks.ftlh");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        sendHtmlEmail(to, htmlBody);
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
