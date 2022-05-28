package com.example.pets_backend;

import com.example.pets_backend.entity.User;
import com.example.pets_backend.repository.UserRepository;
import com.example.pets_backend.service.NtfTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ScheduledJobs {

    private final UserRepository userRepository;
    private final NtfTaskService ntfTaskService;

    @Scheduled(cron = "0 48 14 * * *")    // repeat 4am everyday
    public void ntfUpcomingTasks() {
        for (User user:userRepository.findAll()) {
            ntfTaskService.addTasksNotification(user, false);
        }
    }

    @Scheduled(cron = "0 0 9 * * *")    // repeat 9am everyday
    public void ntfOverdueTasks() {
        for (User user:userRepository.findAll()) {
            ntfTaskService.addTasksNotification(user, true);
        }
    }
}
