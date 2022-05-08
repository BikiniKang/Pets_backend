package com.example.pets_backend.repository;


import com.example.pets_backend.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecordRepository extends JpaRepository<Record, String> {
    Record findByRecordId(String recordId);

    @Modifying
    @Query("delete from Record where recordId = ?1")
    void deleteByRecordId(String recordId);
}
