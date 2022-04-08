package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    private String content;
}
