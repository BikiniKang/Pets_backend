package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "uid", nullable = false, foreignKey = @ForeignKey(name = "fk_task_uid"))
    private User user;

    @NonNull
    @ElementCollection
    @CollectionTable(
            name = "task_petidlist",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<String> petIdList = new ArrayList<>();

    @NonNull
    @Column(length = 64)
    private String taskTitle;

    @NonNull
    @Column(length = 16)
    private String startDate;       // YYYY-MM-DD HH:mm

    @NonNull
    @Column(length = 16)
    private String dueDate;         // YYYY-MM-DD HH:mm

    @NonNull
    private boolean isChecked;
}
