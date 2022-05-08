package com.example.pets_backend.service;


import com.example.pets_backend.entity.Record;
import com.example.pets_backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;

    @Override
    public Record save(Record record) {
        log.info("New record saved into database");
        return recordRepository.save(record);
    }

    @Override
    public Record findByRecordId(String recordId) {
        Record record = recordRepository.findByRecordId(recordId);
        if (record == null) {
            log.error("Record {} not found in the database", recordId);
            throw new IllegalArgumentException("Record " + recordId + " not found in database");
        } else {
            log.info("Record {} found in the database", recordId);
            return record;
        }
    }
}
