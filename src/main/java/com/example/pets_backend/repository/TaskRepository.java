package com.example.pets_backend.repository;


import com.example.pets_backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    Task findByTaskId(String taskId);

    @Modifying
    @Query("delete from Task where taskId = ?1")
    void deleteByTaskId(String taskId);
}
