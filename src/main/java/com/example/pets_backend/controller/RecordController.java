package com.example.pets_backend.controller;

import com.example.pets_backend.service.RecordService;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class RecordController {

    private final UserService userService;
    private final RecordService recordService;


}
