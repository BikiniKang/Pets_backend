package com.example.pets_backend.service;


import com.example.pets_backend.entity.Record;
import com.example.pets_backend.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;

    @Override
    public Record save(Record record) {
        log.info("New record '{}' saved into database", record.getRecordId());
        return recordRepository.save(record);
    }

    @Override
    public Record findByRecordId(String recordId) {
        Record record = recordRepository.findByRecordId(recordId);
        checkRecordInDB(record,recordId);
        return record;
    }

    @Override
    public void deleteByRecordId(String recordId) {
        Record record = recordRepository.findByRecordId(recordId);
        checkRecordInDB(record,recordId);
        recordRepository.deleteByRecordId(recordId);
    }

    @Override
    public List<Record> findAllByUidAndRecordType(String uid, String recordType) {
        return recordRepository.findAllByUidAndRecordType(uid, recordType);
    }

    private void checkRecordInDB(Record record, String recordId) {
        if (record == null) {
            log.error("Record '{}' not found in the database", recordId);
            throw new EntityNotFoundException("Record '" + recordId + "' not found in database");
        } else {
            log.info("Record '{}' found in the database", recordId);
        }
    }
}
