package com.example.pets_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speciesId;

    private String speciesName;

    @JsonIgnore
    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL)
    private List<Breed> breedList = new ArrayList<>();
}