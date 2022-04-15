package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    private String petName;

    private String petAvatar;

    private String species;

    private String breed;

    private String petImg;

    private int gender;

    private String petDob;

    private double weight;

    private double height;

    @ManyToMany
    private List<Task> taskList;

    @ManyToMany
    private List<Task> eventList;
}
