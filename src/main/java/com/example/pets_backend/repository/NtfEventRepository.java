package com.example.pets_backend.repository;


import com.example.pets_backend.entity.NtfEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NtfEventRepository extends JpaRepository<NtfEvent, String> {
}
