package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    private String title;
}
