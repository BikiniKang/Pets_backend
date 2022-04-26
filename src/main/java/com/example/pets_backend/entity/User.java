package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

import static com.example.pets_backend.ConstantValues.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private final String uid = NanoIdUtils.randomNanoId();

    @NonNull
    @Column(length = 32)
    private String email;

    @NonNull
    private String password;

    @NonNull
    @Column(length = 32)
    private String firstName;

    @NonNull
    @Column(length = 32)
    private String lastName;

    @Column(length = 16)
    private String phone = DEFAULT_PHONE;

    private String address = DEFAULT_ADDRESS;

    private String image = DEFAULT_IMAGE;

    private boolean isPetSitter = false;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> petList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> taskList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> eventList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Folder> folderList = new ArrayList<>();


    public List<LinkedHashMap<String, Object>> getPetAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Pet pet:this.petList) {
            list.add(pet.getPetAb());
        }
        return list;
    }

    public List<LinkedHashMap<String, Object>> getFolderAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Folder folder : this.folderList) {
            list.add(folder.getFolderAb());
        }
        return list;
    }
}
