package com.example.pets_backend.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.Record;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.RecordService;
import com.example.pets_backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class RecordController {

    private final UserService userService;
    private final RecordService recordService;
    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/user/record/add")
    public Record addRecord(@RequestBody Map<String, Object> mapIn) {
        User user = userService.findByUid((String) mapIn.get("uid"));
        Record record = mapper.convertValue(mapIn.get("record"), Record.class);
        Pet pet = user.getPetByPetId(record.getPetId());

        record.setRecordId(NanoIdUtils.randomNanoId());
        record.setUser(user);
        record.setPetName(pet.getPetName());
        record.setPetAvatar(pet.getPetAvatar());

        return recordService.save(record);
    }
}
