package com.example.pets_backend.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import static com.example.pets_backend.ConstantValues.TIMEZONE;

/**
 * reference: https://allaboutspringframework.com/spring-schedule-tasks-or-cron-jobs-dynamically/#:~:text=Spring%20provides%20Task%20Scheduler%20API,different%20methods%20to%20schedule%20task.
 */
@Service
public class ScheduleTaskService {

    // Task Scheduler
    TaskScheduler scheduler;

    // A map for keeping scheduled tasks
    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public ScheduleTaskService(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }


    public void addJobToScheduler(String id, Runnable task, LocalDateTime triggerTime) {
        ScheduledFuture<?> scheduledJob = scheduler.schedule(task, triggerTime.atZone(ZoneId.of(TIMEZONE)).toInstant());
        jobsMap.put(id, scheduledJob);
    }

    // Remove scheduled task
    public void removeJobFromScheduler(String id) {
        ScheduledFuture<?> scheduledJob = jobsMap.get(id);
        if(scheduledJob != null) {
            scheduledJob.cancel(true);
            jobsMap.put(id, null);
        }
    }

    // A context refresh event listener
    @EventListener({ ContextRefreshedEvent.class })
    public void contextRefreshedEvent() {
        // Get all tasks from DB and reschedule them in case of context restarted
    }

}
