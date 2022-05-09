package com.example.pets_backend.service;


import com.example.pets_backend.entity.Task;
import com.example.pets_backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        log.info("New task '{}' saved into database", task.getTaskId());
        return taskRepository.save(task);
    }

    @Override
    public Task findByTaskId(String taskId) {
        Task task = taskRepository.findByTaskId(taskId);
        checkTaskInDB(task, taskId);
        return task;
    }

    @Override
    public void deleteByTaskId(String taskId) {
        Task task = taskRepository.findByTaskId(taskId);
        checkTaskInDB(task, taskId);
        taskRepository.deleteByTaskId(taskId);
        log.info("Task '{}' deleted from database", taskId);
    }

    private void checkTaskInDB(Task task, String taskId) {
        if (task == null) {
            log.error("Task '{}' not found in the database", taskId);
            throw new EntityNotFoundException("Task '" + taskId + "' not found in database");
        } else {
            log.info("Task '{}' found in the database", taskId);
        }
    }
}
