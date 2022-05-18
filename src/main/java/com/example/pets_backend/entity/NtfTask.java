package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class NtfTask {

    @Id
    private String ntfId = NanoIdUtils.randomNanoId();

    @NotNull
    private String uid;

    @NonNull
    @ElementCollection
    private List<String> taskIdList = new ArrayList<>();

    @NonNull
    private String ntfDate;
}
