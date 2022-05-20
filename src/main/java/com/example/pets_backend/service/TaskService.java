package com.example.pets_backend.service;


import com.example.pets_backend.entity.Task;

public interface TaskService {
    Task save(Task task);

    Task findByTaskId(String taskId);

    void deleteByTaskId(String taskId);

    void archive(String taskId);
}
