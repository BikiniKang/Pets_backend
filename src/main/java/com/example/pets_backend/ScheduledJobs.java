package com.example.pets_backend;

import com.example.pets_backend.entity.User;
import com.example.pets_backend.repository.UserRepository;
import com.example.pets_backend.service.NtfTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ScheduledJobs {

    private final UserRepository userRepository;
    private final NtfTaskService ntfTaskService;

    @Scheduled(cron = "0 0 4 * * *")    // repeat 4am everyday
    public void ntfUpcomingTasks() {
        List<User> users = userRepository.findAll();
        for (User user:users) {
            if (user.isTaskNtfOn()) {
                ntfTaskService.addTasksNotification(user, false);
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * *")    // repeat 9am everyday
    public void ntfOverdueTasks() {
        List<User> users = userRepository.findAll();
        for (User user:users) {
            if (user.isTaskNtfOn()) {
                ntfTaskService.addTasksNotification(user, true);
            }
        }
    }
}
