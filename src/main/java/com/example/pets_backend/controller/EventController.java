package com.example.pets_backend.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.pets_backend.entity.Event;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.EventService;
import com.example.pets_backend.service.ScheduleTaskService;
import com.example.pets_backend.service.SendMailService;
import com.example.pets_backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.pets_backend.ConstantValues.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class EventController {

    private final UserService userService;
    private final EventService eventService;
    private final ScheduleTaskService scheduleTaskService;
//    private final JavaMailSenderImpl mailSender;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    private final SendMailService sendMailService;

//    private long getDelay(String startDateTime) {
//        LocalDateTime remindTime = LocalDateTime.parse(startDateTime, formatter).minus(REMIND_BEFORE, ChronoUnit.MINUTES);
//        // assume the user is in the same timezone of the server
//        return ChronoUnit.MILLIS.between(LocalDateTime.now(), remindTime);
//    }
//
//    private void sendMail(String typeStr, String receiver, String receiverName, String content) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(TEAM_EMAIL);
//        msg.setTo(receiver);
//        String mailText = "Hi " + receiverName + ", \n" +
//                "You have " + typeStr + " starts in 1 hour: \n" +
//                "        " + content;
//        msg.setText(mailText);
//        msg.setSubject("Pet Pocket Reminder");
//        mailSender.send(msg);
//    }
//
//    @PostMapping("/user/send/email")
//    public void sendEmail(@RequestBody Map<String, Object> mapIn) {
//        String id = (String) mapIn.get("id");
//        String email = (String) mapIn.get("email");
//        String name = (String) mapIn.get("name");
//        String eventTitle = (String) mapIn.get("eventTitle");
//        int delay = (int) mapIn.get("delay");
//        scheduleTaskService.addTaskToScheduler(id, new Runnable() {
//            @Override
//            public void run() {
//                sendMail("an event", email, name, eventTitle);
//                log.info("Email sent!");
//            }
//        }, LocalDateTime.now().plusSeconds(delay));
//    }

    @PostMapping("/user/send/email/html")
    public void sendHtmlEmail(@RequestBody Map<String, Object> mapIn) throws Exception {
        String email = (String) mapIn.get("email");
        List<String> petNameList = (List<String>) mapIn.get("petNameList");
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("firstName", mapIn.get("name"));
        templateModel.put("petNames", String.join(", ", petNameList));
        templateModel.put("eventTitle", mapIn.get("eventTitle"));
        templateModel.put("eventStartTime", mapIn.get("eventStartTime"));
        templateModel.put("eventLocation", mapIn.get("eventLocation"));
        templateModel.put("petAvatar", "images/pet-avatar-example.png");
        sendMailService.sendEmailForEvent(email, "Pet Pocket Reminder", templateModel);
    }

    @PostMapping("/user/event/add")
    public Map<String, Object> addEvent(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        User user = userService.findByUid(uid);
        Event event = mapper.convertValue(mapIn.get("eventData"), Event.class);

        List<String> petIdList = user.getPetIdList();
        if (!petIdList.containsAll(event.getPetIdList())) {
            throw new IllegalArgumentException("One or more petIds do not belong to User '" + uid + "'");
        }
        event.setEventId(NanoIdUtils.randomNanoId());
        event.setUser(user);

        ///////////////////////test mail sending///////////////////////
//        scheduleTaskService.addTaskToScheduler(event.getEventId(), new Runnable() {
//            @Override
//            public void run() {
//                sendMail("an event", user.getEmail(), user.getFirstName(), event.getEventTitle());
//            }
//        }, LocalDateTime.parse(event.getStartDateTime(), formatter).minus(REMIND_BEFORE, ChronoUnit.MINUTES));

        if (mapIn.containsKey("delay")) {  // if the api specifies the delay, send an email to the user in given minutes
            int delay = (int) mapIn.get("delay");
            log.info("Send email to '{}' with delay of {} minute", event.getUser().getEmail(), delay);
            if (delay < 1) { // if delay is less than 1 minute, send email immediately
                try {
                    sendMailService.sendEmailForEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                LocalDateTime scheduleTime = LocalDateTime.parse(event.getStartDateTime(), formatter).minus(REMIND_BEFORE, ChronoUnit.MINUTES);
                LocalDateTime scheduleTime = LocalDateTime.now().plus(delay, ChronoUnit.MINUTES);
                scheduleTaskService.addTaskToScheduler(event.getEventId(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendMailService.sendEmailForEvent(event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, scheduleTime);
            }
        }

        ///////////////////////test mail sending///////////////////////

        eventService.save(event);
        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("event", event);
        return mapOut;
    }

    @DeleteMapping("/user/event/delete")
    public void deleteEvent(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        String eventId = (String) mapIn.get("eventId");
        // check whether the event exists and belongs to the user
        userService.getEventByUidAndEventId(uid, eventId);
        eventService.deleteByEventId(eventId);
    }

    @PostMapping("/user/event/edit")
    @Transactional
    public Map<String, Object> editEvent(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        Event eventNew = mapper.convertValue(mapIn.get("newEventData"), Event.class);
        String eventId = eventNew.getEventId();

        Event event = userService.getEventByUidAndEventId(uid, eventId);
        // update all attributes except eventId, user
        event.setEventType(eventNew.getEventType());
        event.setEventTitle(eventNew.getEventTitle());
        event.setPetIdList(eventNew.getPetIdList());
        event.setDescription(eventNew.getDescription());
        event.setStartDateTime(eventNew.getStartDateTime());
        event.setEndDateTime(eventNew.getEndDateTime());

        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("event", event);
        return mapOut;
    }

    @PostMapping("/user/event/date")
    public Map<String, Object> getEventsByDate(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        User user = userService.findByUid(uid);
        String date = (String) mapIn.get("date");

        List<Event> eventList = user.getEventsByDate(date);

        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("uid", uid);
        mapOut.put("eventList", eventList);
        return mapOut;
    }

    @PostMapping("/user/event")
    public Event getEvent(@RequestBody Map<String, Object> mapIn) {
        String eventId = (String) mapIn.get("eventId");
        return eventService.findByEventId(eventId);
    }

    @PostMapping("/user/event/all")
    public List<Event> getAllEvents(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        User user = userService.findByUid(uid);
        return user.getEventList();
    }
}
