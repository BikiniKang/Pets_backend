package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long breedId;

    private String breedName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "speciesId", nullable = false)
    private Species species;
}