package com.example.pets_backend.repository;


import com.example.pets_backend.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, String> {
}
