package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    @NonNull
    @ElementCollection
    @CollectionTable(
            name = "event_petidlist",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<String> petIdList = new ArrayList<>();

    @NonNull
    @Column(length = 64)
    private String eventTitle;

    @NonNull
    @Column(length = 16)
    private String eventType = DEFAULT_EVENT_TYPE;

    @NonNull
    @Column(length = 16)
    private String startDateTime;      // YYYY-MM-DD HH:mm

    @NonNull
    @Column(length = 16)
    private String endDateTime;        // YYYY-MM-DD HH:mm

    @NonNull
    private String description = "";

    @JsonIgnore
    public LinkedHashMap<String, Object> getEventAb() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("eventId", this.eventId);
        map.put("eventTitle", this.eventTitle);
        return map;
    }

}
