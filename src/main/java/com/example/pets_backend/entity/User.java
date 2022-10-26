package com.example.pets_backend.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.*;

import static com.example.pets_backend.ConstantValues.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Slf4j
public class User {

    @Id
    private final String uid = NanoIdUtils.randomNanoId();      // user ID

    @NonNull
    @Column(length = 32, nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private boolean email_verified = false;

    @JsonIgnore
    private String verify_token = NanoIdUtils.randomNanoId();

    @NonNull
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(length = 32, nullable = false)
    private String firstName;

    @NonNull
    @Column(length = 32, nullable = false)
    private String lastName;

    @Column(length = 16)
    private String phone = DEFAULT_PHONE;

    private String address = DEFAULT_ADDRESS;

    private String image = DEFAULT_IMAGE;

    @Column(nullable = false)
    private boolean isPetSitter = false;

    @Column(nullable = false)
    private boolean eventNtfOn = true;

    @Column(nullable = false)
    private boolean taskNtfOn = true;

    @Column(length = 5, nullable = false)
    private String taskNtfTime  = "18:00";

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

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookingList = new ArrayList<>();


    /**
     * This method acts as a getter.
     * In the json body of a User instance, there's a key-value pair - "petAbList": [...]
     * @return a list of Pets' abstract information
     */
    public List<LinkedHashMap<String, Object>> getPetAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Pet pet:this.petList) {
            list.add(pet.getPetAb());
        }
        return list;
    }

    /**
     * This method acts as a getter.
     * In the json body of a User instance, there's a key-value pair - "eventAbList": [...]
     * @return a list of Events' abstract information
     */
    public List<LinkedHashMap<String, Object>> getEventAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Event event:this.eventList) {
            list.add(event.getEventAb());
        }
        return list;
    }

    /**
     * This method acts as a getter.
     * In the json body of a User instance, there's a key-value pair - "taskAbList": [...]
     * @return a list of Tasks' abstract information
     */
    public List<LinkedHashMap<String, Object>> getTaskAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Task task:this.taskList) {
            list.add(task.getTaskAb());
        }
        return list;
    }

    /**
     * This method acts as a getter.
     * In the json body of a User instance, there's a key-value pair - "recordAbList": [...]
     * @return a list of Records' abstract information
     */
    public List<LinkedHashMap<String, Object>> getRecordAbList() {
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        for (Record record:this.recordList) {
            list.add(record.getRecordAb());
        }
        return list;
    }

    /**
     * Get all the ids of the pets belong to the user.
     * @return a list of Pets' ids
     */
    @JsonIgnore
    public List<String> getPetIdList() {
        List<String> petIdList = new ArrayList<>();
        for (Pet pet:petList) {
            petIdList.add(pet.getPetId());
        }
        return petIdList;
    }

    /**
     * Get the notification settings of the user.
     * @return a map looks like {"eventNtfOn": true, "taskNtfOn": true, "taskNtfTime": "18:00"}
     */
    @JsonIgnore
    public LinkedHashMap<String, Object> getNotificationSettings() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("eventNtfOn", eventNtfOn);
        map.put("taskNtfOn", taskNtfOn);
        map.put("taskNtfTime", taskNtfTime);
        return map;
    }


    /**
     * Get the Pet object by petName
     * @param petName the name of the pet
     * @return a Pet object
     */
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
    public Pet getPetByPetId(String petId) {
        for (Pet pet:this.petList) {
            if (pet.getPetId().equals(petId)) {
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
    public Event getEventByEventId(String eventId) {
        for (Event event:this.eventList) {
            if (event.getEventId().equals(eventId)) {
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
    public Task getTaskByTaskId(String taskId) {
        for (Task task:this.taskList) {
            if (task.getTaskId().equals(taskId)) {
                return task;
            }
        }
        throw new EntityNotFoundException("The Task not found in the User");
    }

    /**
     * Get the Record object by recordId
     * @param recordId the id of the record
     * @return a Record object
     */
    public Record getRecordByRecordId(String recordId) {
        for (Record record:this.recordList) {
            if (record.getRecordId().equals(recordId)) {
                return record;
            }
        }
        throw new EntityNotFoundException("Record '" + recordId + "' not found in User '" + this.uid + "'");
    }

    /**
     * Get all events of the user that startDate <= date <= endDate
     * @param date 'yyyy-MM-dd'
     * @return a list of Event objects, ordered by event start time
     */
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
     * Get all tasks that due on a given date
     * @param date 'yyyy-MM-dd'
     * @return a list of Task objects
     */
    public List<Task> getTasksByDate(String date) {
        List<Task> taskList = new ArrayList<>();
        for (Task task:this.taskList) {
            if (task.getDueDate().equals(date)) {
                taskList.add(task);
            }
        }
        return taskList;
    }

    /**
     * Get all bookings that start or end on the given date
     * @param date 'yyyy-MM-dd'
     * @return a list of Booking objects, ordered by booking start time
     */
    public List<Booking> getBookingsByDate(String date) {
        List<Booking> bookingList = new ArrayList<>();
        for (Booking booking:this.bookingList) {
            if (date.equals(booking.getStart_time().substring(0, DATE_PATTERN.length())) ||
                    date.equals(booking.getEnd_time().substring(0, DATE_PATTERN.length()))) {
                bookingList.add(booking);
            }
        }
        bookingList.sort(Comparator.comparing(Booking::getStart_time));
        return bookingList;
    }

    /**
     * Get all bookings that start or end in the given month
     * @param month 'yyyy-MM'
     * @return a list of maps (each map has keys 'date' and 'bookingList'), ordered by date
     */
    public List<Map<String, Object>> getBookingsByMonth(String month) {
        List<String> dateSpan = getDateSpanOfMonth(month);  // Get all dates of the given month
        Map<String, List<Booking>> cal = new HashMap<>();   // Initialize the <date:bookingList> map
        for (String date:dateSpan) {
            cal.put(date, new ArrayList<>());
        }
        // Iterate bookings, if the booking's start/end date matches a date in the map, add it into the bookingList of that date
        for (Booking b:this.bookingList) {
            String startDate = b.getStart_time().substring(0, DATE_PATTERN.length());
            String endDate = b.getEnd_time().substring(0, DATE_PATTERN.length());
            if (cal.containsKey(startDate)) {
                cal.get(startDate).add(b);
            }
            if (!endDate.equals(startDate) && cal.containsKey(endDate)) {
                cal.get(endDate).add(b);
            }
        }
        // Rearrange the <date:bookingList> map to create a list of maps, each map has keys 'date' and 'bookingList'
        List<Map<String, Object>> list = new ArrayList<>();
        for (String date:dateSpan) {
            Map<String, Object> map = new HashMap<>();
            List<Booking> bookingList = cal.get(date);
            bookingList.sort(Comparator.comparing(Booking::getStart_time)); // sort the bookingList by booking start time
            map.put("date", date);
            map.put("bookingList", bookingList);
            list.add(map);
        }
        return list;
    }

    /**
     * Get a list of Calendar objects containing information of events, tasks, and bookings
     * @param month 'yyyy-MM'
     * @return a list of maps, where each map represents a calendar day in the given month and contains a date('yyyy-MM-dd'), an eventList, a taskList, and a bookingList
     */
    public List<Map<String, Object>> getCalByMonth(String month) {
        List<String> dateSpan = getDateSpanOfMonth(month);  // Get all dates of the given month
        Map<String, List<Event>> eventCal = new HashMap<>();
        Map<String, List<Task>> taskCal = new HashMap<>();
        Map<String, List<Booking>> bookingCal = new HashMap<>();
        // Initialize the three calendar maps <date:eventList/taskList/bookingList>
        for (String date:dateSpan) {
            eventCal.put(date, new ArrayList<>());
            taskCal.put(date, new ArrayList<>());
            bookingCal.put(date, new ArrayList<>());
        }
        for (Event event:eventList) {
            // Extract 'yyyy-MM-dd' from startDateTime and endDateTime
            String from = event.getStartDateTime().substring(0, DATE_PATTERN.length());
            String to = event.getEndDateTime().substring(0, DATE_PATTERN.length());
            // Get the date span of this event within the given month
            List<String> eventDateSpan = getDateSpan(from, to, month);
            // Add the event into every date it spans
            for (String d:eventDateSpan) {
                eventCal.get(d).add(event);
            }
        }
        for (Task task:taskList) {
            String dueDate = task.getDueDate();
            // Add the task into its due date
            if (taskCal.containsKey(dueDate)) {
                taskCal.get(dueDate).add(task);
            }
        }
        for (Booking booking:bookingList) {
            String startDate = booking.getStart_time().substring(0, DATE_PATTERN.length());
            String endDate = booking.getEnd_time().substring(0, DATE_PATTERN.length());
            // Add the booking into its start date and end date
            if (bookingCal.containsKey(startDate)) {
                bookingCal.get(startDate).add(booking);
            }
            if (!endDate.equals(startDate) && bookingCal.containsKey(endDate)) {
                bookingCal.get(endDate).add(booking);
            }
        }
        // Create a list of map, each map contains keys 'date', 'eventList', 'taskList', and 'bookingList'
        List<Map<String, Object>> list = new ArrayList<>();
        for (String date:dateSpan) {
            Map<String, Object> map = new HashMap<>();
            List<Event> eventList = eventCal.get(date);
            List<Task> taskList = taskCal.get(date);
            List<Booking> bookingList = bookingCal.get(date);
            // Sort the events by startDateTime
            eventList.sort(Comparator.comparing(Event::getStartDateTime));
            // Sort the tasks by dueDate
            taskList.sort(Comparator.comparing(Task::getDueDate));
            // Sort the bookings by start_time
            bookingList.sort(Comparator.comparing(Booking::getStart_time));
            map.put("date", date);
            map.put("eventList", eventList);
            map.put("taskList", taskList);
            map.put("bookingList", bookingList);
            list.add(map);
        }
        return list;
    }

    /**
     * Get all unchecked tasks due before today
     * @param today 'yyyy-MM-dd'
     * @return a list of overdue Task objects
     */
    public List<Task> getOverdueTasks(String today) {
        List<Task> list = new ArrayList<>();
        for (Task task:taskList) {
            if (task.getDueDate().compareTo(today) < 0 && !task.isChecked()) {
                list.add(task);
            }
        }
        return list;
    }

    /**
     * Get all unchecked tasks due today
     * @param today 'yyyy-MM-dd'
     * @return a list of upcoming Task objects
     */
    public List<Task> getUpcomingTasks(String today) {
        List<Task> list = new ArrayList<>();
        for (Task task:taskList) {
            if (task.getDueDate().compareTo(today) == 0 && !task.isArchived()) {
                list.add(task);
            }
        }
        return list;
    }
}
