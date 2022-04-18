package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Folder {

    @Id
    private final String folderId = NanoIdUtils.randomNanoId();

    @NonNull
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @NonNull
    private Long parentId;

    @NonNull
    private String folderName;

    @JsonManagedReference
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<Record> recordList = new ArrayList<>();

}
