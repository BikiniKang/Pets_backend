package com.example.pets_backend.service;


import com.example.pets_backend.entity.Task;

public interface TaskService {
    Task save(Task task);

    void deleteByTaskId(String taskId);
}
