package com.example.pets_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @ManyToMany
    List<Pet> petList;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "calDateId", nullable = false)
    private CalendarDate calendarDate;

    private String content;

    private boolean isChecked = false;


    public Long getTaskId() {
        return taskId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CalendarDate getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(CalendarDate calendarDate) {
        this.calendarDate = calendarDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public List<String> getPetNameList() {
        List<String> petNameList = new ArrayList<>();
        for (Pet pet:this.petList) {
            petNameList.add(pet.getPetName());
        }
        return petNameList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
