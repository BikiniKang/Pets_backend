package com.example.pets_backend.service;


import com.example.pets_backend.entity.Record;

import java.util.List;

public interface RecordService {
    Record save(Record record);

    Record findByRecordId(String recordId);

    void deleteByRecordId(String recordId);

    List<Record> findAllByUidAndRecordType(String uid, String recordType);
}
