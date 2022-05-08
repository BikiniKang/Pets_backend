package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

import static com.example.pets_backend.ConstantValues.*;
import static com.example.pets_backend.util.GeneralHelperMethods.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private final String uid = NanoIdUtils.randomNanoId();

    @NonNull
    @Column(length = 32)  //TODO: might can be defined as 'unique' instead of manually check duplicates in user service
    private String email;

    @NonNull
    private String password;

    @NonNull
    @Column(length = 32)
    private String firstName;

    @NonNull
    @Column(length = 32)
    private String lastName;

    @Column(length = 16)
    private String phone = DEFAULT_PHONE;

    private String address = DEFAULT_ADDRESS;

    private String image = DEFAULT_IMAGE;

    private boolean isPetSitter = false;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> petList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> taskList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> eventList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Record> recordList = new ArrayList<>();


    public List<LinkedHashMap<String, Object>> getPetAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Pet pet:this.petList) {
            list.add(pet.getPetAb());
        }
        return list;
    }

    public List<LinkedHashMap<String, Object>> getEventAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Event event:this.eventList) {
            list.add(event.getEventAb());
        }
        return list;
    }

    public List<LinkedHashMap<String, Object>> getTaskAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Task task:this.taskList) {
            list.add(task.getTaskAb());
        }
        return list;
    }

    /**
     * Get the Pet object by petName
     * @param petName the name of the pet
     * @return a Pet object
     */
    @JsonIgnore
    public Pet getPetByPetName(String petName) {
        for (Pet pet:this.petList) {
            if (pet.getPetName().equals(petName)) {
                return pet;
            }
        }
        return null;
    }

    /**
     * Get the Pet object by petId
     * @param petId the id of the pet
     * @return a Pet object
     */
    @JsonIgnore
    public Pet getPetByPetId(String petId) {
        for (Pet pet:this.petList) {
            if (pet.getPetId().equals(petId)) {
                return pet;
            }
        }
        return null;
    }

    /**
     * Get the Event object by eventId
     * @param eventId the id of the event
     * @return an Event object
     */
    @JsonIgnore
    public Event getEventByEventId(String eventId) {
        for (Event event:this.eventList) {
            if (event.getEventId().equals(eventId)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Get the Task object by taskId
     * @param taskId the id of the task
     * @return a Task object
     */
    @JsonIgnore
    public Task getTaskByTaskId(String taskId) {
        for (Task task:this.taskList) {
            if (task.getTaskId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }

    /**
     * Get all Event objects in a given date
     * @param date 'yyyy-MM'
     * @return a list of Event objects
     */
    @JsonIgnore
    public List<Event> getEventsByDate(String date) {
        List<Event> eventList = new ArrayList<>();
        for (Event event:this.eventList) {
            String from = event.getStartDateTime().substring(0, DATE_PATTERN.length());
            String to = event.getEndDateTime().substring(0, DATE_PATTERN.length());
            if (from.compareTo(date) <= 0 && to.compareTo(date) >= 0) {
                eventList.add(event);
            }
        }
        eventList.sort(Comparator.comparing(Event::getStartDateTime));
        return eventList;
    }

    /**
     * Get all Task objects in a given date
     * @param date 'yyyy-MM'
     * @return a list of Task objects
     */
    @JsonIgnore
    public List<Task> getTasksByDate(String date) {
        List<Task> taskList = new ArrayList<>();
        for (Task task:this.taskList) {
            String from = task.getStartDate();
            String to = task.getDueDate();
            if (from.compareTo(date) <= 0 && to.compareTo(date) >= 0) {
                taskList.add(task);
            }
        }
        taskList.sort(Comparator.comparing(Task::getDueDate));
        return taskList;
    }

    /**
     * Get a list of Calendar objects containing information of events and tasks
     * @param month 'yyyy-MM'
     * @return a list of maps, where each map represents a calendar day in the given month
     *          and contains a date('yyyy-MM-dd'), an eventList(a list of Event objects),
     *          a taskList(a list of Task objects)
     */
    @JsonIgnore
    public List<Map<String, Object>> getCalByMonth(String month) {
        // get all dates in the given month
        List<String> dateSpan = getDateSpanOfMonth(month);
        Map<String, List<Event>> eventCal = new HashMap<>();
        Map<String, List<Task>> taskCal = new HashMap<>();
        // initialize the event and task calendar map (key: date, value: a list of event/task entities)
        for (String date:dateSpan) {
            eventCal.put(date, new ArrayList<>());
            taskCal.put(date, new ArrayList<>());
        }
        for (Event event:eventList) {
            // extract 'yyyy-MM-dd' from startDateTime and endDateTime
            String from = event.getStartDateTime().substring(0, DATE_PATTERN.length());
            String to = event.getEndDateTime().substring(0, DATE_PATTERN.length());
            // get the date span of this event within the given month
            List<String> eventDateSpan = getDateSpan(from, to, month);
            // add the event into each day it spans
            for (String d:eventDateSpan) {
                eventCal.get(d).add(event);
            }
        }
        for (Task task:taskList) {
            String from = task.getStartDate();
            String to = task.getDueDate();
            // get the date span of this task within the given month
            List<String> taskDateSpan = getDateSpan(from, to, month);
            // add the task into each day it spans
            for (String d:taskDateSpan) {
                taskCal.get(d).add(task);
            }
        }
        // create a list of map, each map contains 'date', 'eventList', and 'taskList' for a specific day in the given month
        List<Map<String, Object>> list = new ArrayList<>();
        for (String date:dateSpan) {
            Map<String, Object> map = new HashMap<>();
            List<Event> eventList = eventCal.get(date);
            List<Task> taskList = taskCal.get(date);
            // sort the events in this day by startDateTime
            eventList.sort(Comparator.comparing(Event::getStartDateTime));
            // sort the tasks in this day by dueDate
            taskList.sort(Comparator.comparing(Task::getDueDate));
            map.put("date", date);
            map.put("eventList", eventList);
            map.put("taskList", taskList);
            list.add(map);
        }
        return list;
    }
}
