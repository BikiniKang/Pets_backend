package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    private String booking_id = NanoIdUtils.randomNanoId();

    @JsonIncludeProperties("uid")
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false, foreignKey = @ForeignKey(name = "fk_booking_uid"))
    private User user;

    @ElementCollection
    @CollectionTable(
            name = "booking_petidlist",
            joinColumns = @JoinColumn(name = "booking_id")
    )
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<String> petIdList = new ArrayList<>();

    @NonNull
    @Column(length = 32)
    private String invitee;

    @NonNull
    @Column(length = 64)
    private String title;

    @NonNull
    @Column(length = 16)
    private String start_time;  // YYYY-MM-DD HH:mm

    @NonNull
    @Column(length = 16)
    private String end_time;    // YYYY-MM-DD HH:mm

    @NonNull
    private String location;

    @NonNull
    private String description;

    @NonNull
    @Column(length = 16)
    private String status;
}
