package com.example.pets_backend.service;


import com.example.pets_backend.entity.Task;

import java.util.List;

public interface TaskService {
    Task save(Task task);

    Task findByTaskId(String taskId);

    void deleteByTaskId(String taskId);

    void archive(String taskId);

    List<Task> findUpcomingTasks(String uid, String today);

    List<Task> findOverdueTasks(String uid, String today);
}
