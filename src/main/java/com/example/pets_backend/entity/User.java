package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.*;

import static com.example.pets_backend.ConstantValues.*;
import static com.example.pets_backend.util.GeneralHelperMethods.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Slf4j
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

    public List<LinkedHashMap<String, Object>> getRecordAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Record record:this.recordList) {
            list.add(record.getRecordAb());
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
                log.info("Pet with name '{}' found in User '{}'", petName, this.uid);
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
                log.info("Pet '{}' found in User '{}'", pet.getPetId(), this.uid);
                return pet;
            }
        }
        throw new EntityNotFoundException("Pet '" + petId + "' not found in User '" + this.uid + "'");
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
                log.info("Event '{}' found in User '{}'", eventId, this.uid);
                return event;
            }
        }
        throw new EntityNotFoundException("Event '" + eventId + "' not found in User '" + this.uid + "'");
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
                log.info("Task '{}' found in User '{}'", taskId, this.uid);
                return task;
            }
        }
        throw new EntityNotFoundException("Task '" + taskId + "' not found in User '" + this.uid + "'");
    }

    /**
     * Get the Record object by recordId
     * @param recordId the id of the record
     * @return a Record object
     */
    @JsonIgnore
    public Record getRecordByRecordId(String recordId) {
        for (Record record:this.recordList) {
            if (record.getRecordId().equals(recordId)) {
                log.info("Record '{}' found in User '{}'", recordId, this.uid);
                return record;
            }
        }
        throw new EntityNotFoundException("Record '" + recordId + "' not found in User '" + this.uid + "'");
    }

    /**
     * Get all Event objects in a given date
     * @param date 'yyyy-MM-dd'
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
     * @param date 'yyyy-MM-dd'
     * @return a list of Task objects
     */
    @JsonIgnore
    public List<Task> getTasksByDate(String date) {
        List<Task> taskList = new ArrayList<>();
        for (Task task:this.taskList) {
            if (task.getDueDate().equals(date)) {
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
            String dueDate = task.getDueDate();
            if (taskCal.containsKey(dueDate)) {
                taskCal.get(dueDate).add(task);
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
