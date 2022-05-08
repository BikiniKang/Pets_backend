package com.example.pets_backend.entity;


import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Record {

    @Id
    private final String recordId = NanoIdUtils.randomNanoId();
}
