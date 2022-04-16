package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

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

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CalendarDate> calendarDateList = new ArrayList<>();


    public List<LinkedHashMap<String, Object>> getPetListAb() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Pet pet:this.petList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("petId", pet.getPetId());
            map.put("petName", pet.getPetName());
            map.put("petAvatar", pet.getPetAvatar());
            list.add(map);
        }
        return list;
    }

    public List<LinkedHashMap<String, Object>> getFolderListAb() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Folder folder : this.folderList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("folderId", folder.getFolderId());
            map.put("folderName", folder.getFolderName());
            list.add(map);
        }
        return list;
    }
}
