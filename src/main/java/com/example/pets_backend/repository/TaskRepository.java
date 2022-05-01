package com.example.pets_backend.repository;


import com.example.pets_backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {
    Task findByTaskId(String taskId);
}
