package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @NonNull
    @Column(length = 32)
    private String email;

    @NonNull
    @Column(length = 16)
    private String password;

    @NonNull
    @Column(length = 32)
    private String firstName;

    @NonNull
    @Column(length = 32)
    private String lastName;

    @Column(length = 16)
    private String phone;

    private String address;

    private String image;

    private boolean isPetSitter = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> petList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> taskList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> eventList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Folder> folderList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CalendarDate> calendarDateList = new ArrayList<>();


    public List<Map<String, Object>> getPetListSub() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Pet pet:this.petList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("petId", pet.getPetId());
            map.put("petName", pet.getPetName());
            map.put("petAvatar", pet.getPetAvatar());
            list.add(map);
        }
        return list;
    }
}
