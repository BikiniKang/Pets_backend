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

    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    private String email;

    @Column(name = "password", nullable = false)
    @NonNull
    private String password;

    @Column(name = "first_name", nullable = false, length = 35)
    @NonNull
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 35)
    @NonNull
    private String lastName;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;

    @Column(name = "pet_sitter")
    private boolean isPetSitter = false;

    @JsonManagedReference
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

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

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
