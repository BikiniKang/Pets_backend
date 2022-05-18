package com.example.pets_backend.service;

import com.example.pets_backend.entity.Event;
import com.example.pets_backend.entity.NtfEvent;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.repository.NtfEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NtfEventService {

    private final NtfEventRepository ntfRepo;
    private final SchedulerService schedulerService;
    private final SendMailService sendMailService;

    public void addEventNotification (Event event, LocalDateTime remindTime) {
        User user = event.getUser();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        Map<String, String> templateModel = generateTemplateModel(firstName, event);

        NtfEvent ntfEvent = new NtfEvent();
        ntfEvent.setUid(user.getUid());
        ntfEvent.setEventId(event.getEventId());
        ntfEvent.setNtfTime(remindTime);
        ntfRepo.save(ntfEvent);
        String ntfId = ntfEvent.getNtfId();

        schedulerService.addJobToScheduler(ntfId, new Runnable() {
            @Override
            public void run() {
                try {
                    sendMailService.sendEmailForEvent(email, templateModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, remindTime);
    }

    private Map<String, String> generateTemplateModel(String firstName, Event event) {
        Map<String, String> templateModel = new HashMap<>();
        templateModel.put("firstName", firstName);
        templateModel.put("petNames", String.join(", ", event.getPetNameList()));
        templateModel.put("eventTitle", event.getEventTitle());
        templateModel.put("eventStartTime", event.getStartDateTime());
        templateModel.put("eventLocation", event.getDescription());
        templateModel.put("petAvatar", (String) event.getPetAbList().get(0).get("petAvatar"));
        return templateModel;
    }
}
