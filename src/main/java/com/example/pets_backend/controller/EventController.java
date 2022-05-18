package com.example.pets_backend.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.pets_backend.entity.Event;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.EventService;
import com.example.pets_backend.service.SchedulerService;
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
    private final SchedulerService schedulerService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    private final SendMailService sendMailService;

    // Temporary API for testing
    @PostMapping("/user/send/email/html")
    public void sendHtmlEmail(@RequestBody Map<String, Object> mapIn) throws Exception {
        String email = (String) mapIn.get("email");
        List<String> petNameList = (List<String>) mapIn.get("petNameList");
        Map<String, String> templateModel = new HashMap<>();
        templateModel.put("firstName", (String) mapIn.get("name"));
        templateModel.put("petNames", String.join(", ", petNameList));
        templateModel.put("eventTitle", (String) mapIn.get("eventTitle"));
        templateModel.put("eventStartTime", (String) mapIn.get("eventStartTime"));
        templateModel.put("eventLocation", (String) mapIn.get("eventLocation"));
        templateModel.put("petAvatar", (String) mapIn.get("petAvatar"));
        sendMailService.sendEmailForEvent(email, templateModel);
    }

    @PostMapping("/user/event/add")
    public Map<String, Object> addEvent(@RequestBody Map<String, Object> mapIn) {
        LocalDateTime timeNow = LocalDateTime.now();
        String uid = (String) mapIn.get("uid");
        User user = userService.findByUid(uid);
        Event event = mapper.convertValue(mapIn.get("eventData"), Event.class);

        List<String> petIdList = user.getPetIdList();
        if (!petIdList.containsAll(event.getPetIdList())) {
            throw new IllegalArgumentException("One or more petIds do not belong to User '" + uid + "'");
        }
        event.setEventId(NanoIdUtils.randomNanoId());
        event.setUser(user);
        eventService.save(event);

//        LocalDateTime remindTime = LocalDateTime.parse(event.getStartDateTime(), formatter).minus(REMIND_BEFORE, ChronoUnit.MINUTES);
//        if (timeNow.isAfter(LocalDateTime.parse(event.getEndDateTime(), formatter))) {  // if the event end time is before now, do not notify
//            log.info("Event has ended, do not notify the user");
//        } else {
//            log.info("Adding notification at {} into scheduler", remindTime);
//            schedulerService.addJobToScheduler(event.getEventId(), new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        sendMailService.sendEmailForEvent(event);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, remindTime); // remind time is 1 hour before the event begins. If remind time is before 'now', send the notification directly
//        }

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
        Event eventNew = mapper.convertValue(mapIn.get("eventData"), Event.class);
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
