package com.example.pets_backend.service;

import com.example.pets_backend.entity.Booking;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.*;

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

    public void sendBookingConfirmEmail(Booking booking, String to, Map<String, String> templateModel, String fromName) throws Exception {
        log.info("Sending booking confirm email to '{}'", to);
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate(TEMPLATE_BOOKING_CONFIRM);
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        sendBookingConfirm(booking, to, htmlBody, fromName);
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

    private void sendBookingConfirm(Booking booking, String to, String htmlBody, String fromName) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(TEAM_EMAIL);
        helper.setTo(to);
        helper.setSubject("Pet Pocket Reminder");

        MimeBodyPart iCalPart = new MimeBodyPart();
        DataSource iCalData = new ByteArrayDataSource(getRawIcs(booking, fromName), "text/calendar; charset=UTF-8");
        iCalPart.setDataHandler(new DataHandler(iCalData));
        iCalPart.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=PUBLISH");
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html");
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(iCalPart);
        multipart.addBodyPart(htmlPart);

        message.setContent(multipart);
        mailSender.send(message);
        log.info("Email has been sent to '{}'", to);
    }

    private String getRawIcs(Booking booking, String fromName) {
        String start = transformTimeFormat(booking.getStart_time());
        String end = transformTimeFormat(booking.getEnd_time());
        String stamp = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ofPattern(ICS_TIME_PATTERN));
        String description = booking.getDescription();
        String id = booking.getBooking_id();
        String location = booking.getLocation();
        String email = booking.getAttendee();
        return "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:-//TechLauncher//Pets Pocket//EN\n" +
                "METHOD:PUBLISH\n" +
                "BEGIN:VEVENT\n" +
                "DTSTART:" + start + "\n" +
                "DTEND:" + end + "\n" +
                "DTSTAMP:" + stamp + "\n" +
                "SUMMARY:Pet Pocket - Appointment\n" +
                "DESCRIPTION:" + description + "\n" +
                "UID:" + id + "\n" +
                "CATEGORIES:Pet Pocket\n" +
                "LOCATION:" + location + "\n" +
                "CREATED:00010101T000000\n" +
                "LAST-MODIFIED:00010101T000000\n" +
                "ORGANIZER;CN=" + fromName + ":MAILTO:" + email + "\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";
    }

    private String transformTimeFormat(String dateTime) {
        ZonedDateTime utcTime = LocalDateTime.parse(dateTime,
                DateTimeFormatter.ofPattern(DATETIME_PATTERN))
                .atZone(ZoneId.of("Australia/Sydney"))
                .withZoneSameInstant(ZoneId.of("UTC"));
        return utcTime.format(DateTimeFormatter.ofPattern(ICS_TIME_PATTERN));
    }

}
