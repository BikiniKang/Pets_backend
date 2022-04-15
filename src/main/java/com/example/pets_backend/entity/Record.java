package com.example.pets_backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "folderId", nullable = false)
    private Folder folder;
}
