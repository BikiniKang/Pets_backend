package com.example.pets_backend.entity;


import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class CalendarDate {

    @Id
    private final String calDateId = NanoIdUtils.randomNanoId();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @OneToMany(mappedBy = "calendarDate", cascade = CascadeType.ALL)
    private List<Task> taskList = new ArrayList<>();

    @OneToMany(mappedBy = "calendarDate", cascade = CascadeType.ALL)
    private List<Event> eventList = new ArrayList<>();

    @NonNull
    private Long dateValue;

    public LinkedHashMap<String, Object> getCalDateAb() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("calDateId", this.calDateId);
        map.put("dataValue", this.dateValue);
        map.put("taskList", this.getTaskListAb());
        map.put("eventList", this.getEventListAb());
        return map;
    }

    public List<LinkedHashMap<String, Object>> getTaskListAb() {
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

    public List<LinkedHashMap<String, Object>> getEventListAb() {
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
