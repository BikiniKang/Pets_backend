package com.example.pets_backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Entity
public class CalendarDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calDateId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    private Long dateValue;

    @JsonManagedReference
    @OneToMany(mappedBy = "calendarDate", cascade = CascadeType.ALL)
    private List<Task> taskList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "calendarDate", cascade = CascadeType.ALL)
    private List<Event> eventList = new ArrayList<>();

    public Long getCalDateId() {
        return calDateId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getDateValue() {
        return dateValue;
    }

    public void setDateValue(Long dateValue) {
        this.dateValue = dateValue;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<LinkedHashMap<String, Object>> getTaskListSub() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Task task:this.taskList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("taskId", task.getTaskId());
            map.put("taskContent", task.getContent());
            map.put("petNameList", task.getPetNameList());
            map.put("isChecked", task.isChecked());
            list.add(map);
        }
        return list;
    }

    public List<LinkedHashMap<String, Object>> getEventListSub() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Event event:this.eventList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("eventId", event.getEventId());
            map.put("eventTitle", event.getEventTitle());
            map.put("startTime", event.getStartTime());
            map.put("endTime", event.getEndTime());
            map.put("petNameList", event.getPetNameList());
            list.add(map);
        }
        return list;
    }
}
