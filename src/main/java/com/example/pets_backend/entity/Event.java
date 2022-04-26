package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
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
    private final String eventId = NanoIdUtils.randomNanoId();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false, foreignKey = @ForeignKey(name = "fk_event_uid"))
    private User user;

    @ElementCollection
    @CollectionTable(
            name = "event_petidlist",
            joinColumns = @JoinColumn(name = "event_id")
    )
    private List<String> petIdList = new ArrayList<>();

    @NonNull
    @Column(length = 64)
    private String eventTitle;

    // TODO: need to modify the following four attributes
    private String startDate = DEFAULT_START_TIME;
    private String startTime = DEFAULT_START_TIME;
    private String endDate = DEFAULT_END_TIME;
    private String endTime = DEFAULT_END_TIME;

    private String eventNote = "";

    private String eventType = DEFAULT_EVENT_TYPE;

}
