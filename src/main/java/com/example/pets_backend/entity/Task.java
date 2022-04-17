package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    private final String taskId = NanoIdUtils.randomNanoId();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "calDateId", nullable = false)
    private CalendarDate calendarDate;

    @JsonIgnore
    @ManyToMany
    private List<Pet> petList;

    @NonNull
    private String content;

    private boolean isChecked = false;


    public List<String> getPetNameList() {
        List<String> petNameList = new ArrayList<>();
        for (Pet pet:this.petList) {
            petNameList.add(pet.getPetName());
        }
        return petNameList;
    }
}
