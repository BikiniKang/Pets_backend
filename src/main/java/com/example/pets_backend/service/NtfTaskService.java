package com.example.pets_backend.service;


import com.example.pets_backend.entity.NtfTask;
import com.example.pets_backend.entity.Task;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.repository.NtfTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.DATETIME_PATTERN;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NtfTaskService {

    private final NtfTaskRepository ntfRepo;
    private final SchedulerService schedulerService;
    private final SendMailService sendMailService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);


    public void addTasksNtfForOneUser(User user, String dueDate) {
        List<Task> taskList = user.getTasksByDate(dueDate);
        String email = user.getEmail();
        String firstName = user.getFirstName();
        Map<String, String> templateModel = generateTemplateModel(firstName, taskList);
        LocalDateTime ntfTime = LocalDateTime.parse(dueDate + " " + user.getTaskNtfTime(), formatter);

        NtfTask ntfTask = new NtfTask();
        ntfTask.setNtfDate(dueDate);
        ntfTask.setUid(user.getUid());
        ntfTask.setTaskIdList(taskList.stream().map(Task::getTaskId).toList());
        ntfRepo.save(ntfTask);
        String ntfId = ntfTask.getNtfId();

        schedulerService.addJobToScheduler(ntfId, new Runnable() {
            @Override
            public void run() {
                try {
                    sendMailService.sendEmailForTasks(email, templateModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, ntfTime);
    }

    private Map<String, String> generateTemplateModel(String firstName, List<Task> taskList) {
        Map<String, String> templateModel = new HashMap<>();
        templateModel.put("firstName", firstName);
        templateModel.put("task1", "");
        templateModel.put("task2", "");
        templateModel.put("task3", "");
        templateModel.put("task1Pets", "");
        templateModel.put("task2Pets", "");
        templateModel.put("task3Pets", "");
        if (taskList.size() > 0) {
            templateModel.put("task1", taskList.get(0).getTaskTitle());
            templateModel.put("task1Pets", "(" + String.join(", ", taskList.get(0).getPetNameList()) + ")");
        }
        if (taskList.size() > 1) {
            templateModel.put("task2", taskList.get(1).getTaskTitle());
            templateModel.put("task2Pets", "(" + String.join(", ", taskList.get(1).getPetNameList()) + ")");
        }
        if (taskList.size() > 2) {
            templateModel.put("task3", taskList.get(1).getTaskTitle());
            templateModel.put("task3Pets", "(" + String.join(", ", taskList.get(1).getPetNameList()) + ")");
        }
        return templateModel;
    }

}
