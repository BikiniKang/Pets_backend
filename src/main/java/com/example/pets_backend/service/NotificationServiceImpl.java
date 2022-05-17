package com.example.pets_backend.service;

import com.example.pets_backend.entity.Event;
import com.example.pets_backend.entity.Notification;
import com.example.pets_backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository ntfRepo;
    private final ScheduleTaskService scheduleTaskService;
    private final SendMailService sendMailService;

    private void addEventNotification (Event event, LocalDateTime remindTime) {
        Notification notification = new Notification();
        notification = ntfRepo.save(notification);
        String ntfId = notification.getNtfId();
        scheduleTaskService.addTaskToScheduler(ntfId, new Runnable() {
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
