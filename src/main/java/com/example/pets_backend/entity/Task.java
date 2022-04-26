package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static com.example.pets_backend.ConstantValues.DEFAULT_END_TIME;
import static com.example.pets_backend.ConstantValues.DEFAULT_START_TIME;

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
    @JoinColumn(name = "uid", nullable = false, foreignKey = @ForeignKey(name = "fk_task_uid"))
    private User user;

    @NonNull
    private String petId;

    @NonNull
    private String content;

    // TODO: need to modify the following four attributes
    private String startDate = DEFAULT_START_TIME;
    private String startTime = DEFAULT_START_TIME;
    private String endDate = DEFAULT_END_TIME;
    private String endTime = DEFAULT_END_TIME;

    private boolean isChecked = false;
}
