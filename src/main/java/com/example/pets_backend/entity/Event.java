package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.pets_backend.ConstantValues.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

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
    @Column(length = 64)
    private String eventTitle;

    private String startTime = DEFAULT_START_TIME;

    private String endTime = DEFAULT_END_TIME;

    private String eventNote = "";

    private String eventType = DEFAULT_EVENT_TYPE;


    public List<String> getPetNameList() {
        List<String> petNameList = new ArrayList<>();
        for (Pet pet:this.petList) {
            petNameList.add(pet.getPetName());
        }
        return petNameList;
    }
}
