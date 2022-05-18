package com.example.pets_backend.service;

import com.example.pets_backend.entity.Event;
import com.example.pets_backend.entity.NtfEvent;
import com.example.pets_backend.repository.NtfEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.pets_backend.ConstantValues.DATE_PATTERN;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NtfEventServiceImpl implements NotificationService {

    private final NtfEventRepository ntfRepo;
    private final ScheduleTaskService scheduleTaskService;
    private final SendMailService sendMailService;

    private void addEventNotification (Event event, LocalDateTime remindTime) {
        NtfEvent ntfEvent = new NtfEvent();
        ntfEvent = ntfRepo.save(ntfEvent);
        String ntfId = ntfEvent.getNtfId();
        scheduleTaskService.addJobToScheduler(ntfId, new Runnable() {
            @Override
            public void run() {
                try {
                    sendMailService.sendEmailForEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, remindTime);
    }
}
