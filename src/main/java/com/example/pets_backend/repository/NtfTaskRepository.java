package com.example.pets_backend.repository;


import com.example.pets_backend.entity.NtfTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NtfTaskRepository extends JpaRepository<NtfTask, String> {
}
