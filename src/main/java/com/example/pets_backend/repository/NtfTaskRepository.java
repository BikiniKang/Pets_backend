package com.example.pets_backend.repository;


import com.example.pets_backend.entity.NtfTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NtfTaskRepository extends JpaRepository<NtfTask, String> {

    @Modifying
    @Query("delete from NtfTask where ntfId = ?1")
    void deleteByNtfId(String ntfId);
}
