package com.example.pets_backend.controller;

import com.example.pets_backend.entity.Event;
import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.Task;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.EventService;
import com.example.pets_backend.service.PetService;
import com.example.pets_backend.service.TaskService;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserController {

    private final UserService userService;
    private final PetService petService;
    private final EventService eventService;
    private final TaskService taskService;

    @PostMapping(REGISTER)
    public LinkedHashMap<String, Object> register(@RequestBody Map<String, Object> mapIn) {
        User user = new User((String) mapIn.get("email"), (String) mapIn.get("password"), (String) mapIn.get("firstName"), (String) mapIn.get("lastName"));
        User savedUser = userService.save(user);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("uid", savedUser.getUid());
        return map;
    }

    @DeleteMapping("/user/delete")
    public void deleteUser(@RequestBody Map<String, Object> mapIn) {
        userService.deleteById((String) mapIn.get("uid"));
    }

    @PostMapping("/user/all")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/user/dashboard")
    public LinkedHashMap<String, Object> getUserDashboard(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("firstName", user.getFirstName());
        mapOut.put("lastName", user.getLastName());
        mapOut.put("image", user.getImage());
        mapOut.put("petList", user.getPetAbList());
        mapOut.put("folderList", user.getFolderAbList());
//        if (user.getCalendarDateList().size() != 0) {
//            //TODO: need to get today's calendarDate object
//            CalendarDate calendarDate = user.getCalendarDateList().get(0);
//            mapOut.put("calendarDate", calendarDate.getCalDateAb());
//        } else {
//            mapOut.put("calendarDate", null);
//        }
        return mapOut;
    }

    @PostMapping("/user/profile")
    public LinkedHashMap<String, Object> getUserProfile(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("firstName", user.getFirstName());
        mapOut.put("lastName", user.getLastName());
        mapOut.put("image", user.getImage());
        mapOut.put("email", user.getEmail());
        mapOut.put("phone", user.getPhone());
        mapOut.put("address", user.getAddress());
        mapOut.put("isPetSitter", user.isPetSitter());
        mapOut.put("petList", user.getPetAbList());
        return mapOut;
    }

    @PostMapping("/user/profile/update")
    @Transactional
    public void updateUserProfile(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        user.setFirstName((String) mapIn.get("firstName"));
        user.setLastName((String) mapIn.get("lastName"));
        user.setPhone((String) mapIn.get("phone"));
        user.setAddress((String) mapIn.get("address"));
        user.setPetSitter((boolean) mapIn.get("isPetSitter"));
    }

    @PostMapping("/user/profile/image/update")
    @Transactional
    public void updateUserProfileImage(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        user.setImage((String) mapIn.get("image"));
    }

    @PostMapping("/user/pet/add")
    @Transactional
    public LinkedHashMap<String, Object> addPet(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        String petAvatar = (String) mapIn.get("petAvatar");
        if (petAvatar == null || petAvatar.length() == 0) petAvatar = DEFAULT_IMAGE_PET;
        Pet pet = new Pet(user, (String) mapIn.get("petName"), petAvatar,
                (String) mapIn.get("species"), (String) mapIn.get("breed"), (int) mapIn.get("gender"), (String) mapIn.get("petDob"));
        if (mapIn.containsKey("weight")) pet.setWeight((int) mapIn.get("weight"));
        if (mapIn.containsKey("height")) pet.setHeight((int) mapIn.get("height"));
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Pet savedPet = petService.save(pet);
        map.put("petId", savedPet.getPetId());
        return map;
    }

    @PostMapping("/user/pet/profile")
    public LinkedHashMap<String, Object> getPet(@RequestBody Map<String, Object> mapIn) {
        Pet pet = petService.findByPetId((String) mapIn.get("petId"));
        String uid = (String) mapIn.get("uid");
        if (!uid.equals(pet.getUser().getUid())) {
            log.error("Pet {} does not belongs to user {}", pet.getPetId(), mapIn.get("uid"));
            throw new IllegalArgumentException("Pet " + pet.getPetId() + " does not belongs to user " + mapIn.get("uid"));
        }
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("petName", pet.getPetName());
        mapOut.put("petAvatar", pet.getPetAvatar());
        mapOut.put("gender", pet.getGender());
        mapOut.put("petDob", pet.getPetDob());
        mapOut.put("species", pet.getSpecies());
        mapOut.put("breed", pet.getBreed());
        mapOut.put("weight", pet.getWeight());
        mapOut.put("height", pet.getHeight());
        return mapOut;
    }

    @PostMapping("/user/pet/profile/update")
    @Transactional
    public void updatePet(@RequestBody Map<String, Object> mapIn) {
        Pet pet = petService.findByPetId((String) mapIn.get("petId"));
        if (!mapIn.get("uid").equals(pet.getUser().getUid())) {
            log.error("Pet {} does not belongs to user {}", pet.getPetId(), mapIn.get("uid"));
            throw new IllegalArgumentException("Pet " + pet.getPetId() + " does not belongs to user " + mapIn.get("uid"));
        }
        pet.setPetName((String) mapIn.get("petName"));
        pet.setPetAvatar((String) mapIn.get("petAvatar"));
        pet.setGender((int) mapIn.get("gender"));
        pet.setSpecies((String) mapIn.get("species"));
        pet.setBreed((String) mapIn.get("breed"));
        pet.setPetDob((String) mapIn.get("petDob"));
        if (mapIn.containsKey("weight")) pet.setWeight((int) mapIn.get("weight"));
        if (mapIn.containsKey("height")) pet.setHeight((int) mapIn.get("height"));
    }

    @DeleteMapping("/user/pet/delete")
    public void deletePet(@RequestBody Map<String, Object> mapIn) {
        String petId = (String) mapIn.get("petId");
        Pet pet = petService.findByPetId(petId);
        if (!mapIn.get("uid").equals(pet.getUser().getUid())) {
            log.error("Pet {} does not belongs to user {}", pet.getPetId(), mapIn.get("uid"));
            throw new IllegalArgumentException("Pet " + pet.getPetId() + " does not belongs to user " + mapIn.get("uid"));
        }
        petService.deleteByPetId(petId);
    }

    @PostMapping("/user/event/add")
    public Map<String, Object> addEvent(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        User user = userService.getById(uid);
        LinkedHashMap<String, Object> eventData = (LinkedHashMap<String, Object>) mapIn.get("eventData");
        Event event = new Event();
        loadEvent(eventData, event);
        event.setUser(user);
        event = eventService.save(event);
        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("event", event);
        return mapOut;
    }

    @DeleteMapping("/user/event/delete")
    public void deleteEvent(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        String eventId = (String) mapIn.get("eventId");
        userService.getEventByUidAndEventId(uid, eventId);
        eventService.deleteByEventId(eventId);
    }

    @PostMapping("/user/event/edit")
    @Transactional
    public Map<String, Object> editEvent(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        LinkedHashMap<String, Object> newEventData = (LinkedHashMap<String, Object>) mapIn.get("newEventData");
        String eventId = (String) newEventData.get("eventId");
        Event event = userService.getEventByUidAndEventId(uid, eventId);
        loadEvent(newEventData, event);
        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("event", event);
        return mapOut;
    }

    @PostMapping("/user/event/all")
    public Map<String, Object> getEventsByDate(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        User user = userService.getById(uid);
        String date = (String) mapIn.get("date");
        List<Event> eventList = user.getEventsByDate(date);
        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("userId", uid);
        mapOut.put("eventList", eventList);
        return mapOut;
    }

    @PostMapping("/user/task/add")
    public Map<String, Object> addTask(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        User user = userService.getById(uid);
        Task task = new Task();
        LinkedHashMap<String, Object> taskData = (LinkedHashMap<String, Object>) mapIn.get("taskData");
        task.setPetIdList((List<String>) taskData.get("petIdList"));
        task.setTaskTitle((String) taskData.get("taskTitle"));
        task.setStartDate((String) taskData.get("startDate"));
        task.setDueDate((String) taskData.get("dueDate"));
        task.setChecked(false);
        task.setUser(user);
        task = taskService.save(task);
        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("task", task);
        return mapOut;
    }

    @DeleteMapping("/user/task/delete")
    public void deleteTask(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        String taskId = (String) mapIn.get("taskId");
        userService.getTaskByUidAndTaskId(uid, taskId);
        taskService.deleteByTaskId(taskId);
    }

    @PostMapping("/user/task/edit")
    @Transactional
    public Task editTask(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        Task taskNew = (Task) mapIn.get("newTaskData");
        Task task = userService.getTaskByUidAndTaskId(uid, taskNew.getTaskId());
        task.setTaskTitle(taskNew.getTaskTitle());
        task.setPetIdList(taskNew.getPetIdList());
        task.setStartDate(taskNew.getStartDate());
        task.setDueDate(taskNew.getDueDate());
        task.setChecked(taskNew.isChecked());
        return task;
    }

    @PostMapping("/user/task/check")
    @Transactional
    public Task checkTask(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        String taskId = (String) mapIn.get("taskId");
        Task task = userService.getTaskByUidAndTaskId(uid, taskId);
        int isChecked = (int) mapIn.get("isChecked");
        task.setChecked(isChecked != 0);
        return task;
    }

    @PostMapping("/user/task/all")
    public Map<String, Object> getTasksByDate(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("userId");
        User user = userService.getById(uid);
        String date = (String) mapIn.get("date");
        List<Task> taskList = user.getTasksByDate(date);
        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("userId", uid);
        mapOut.put("taskList", taskList);
        return mapOut;
    }


    private void loadEvent(LinkedHashMap<String, Object> newEventData, Event event) {
        event.setPetIdList((List<String>) newEventData.get("petIdList"));
        event.setEventTitle((String) newEventData.get("eventTitle"));
        event.setEventType((String) newEventData.get("eventType"));
        event.setStartDateTime((String) newEventData.get("startDateTime"));
        event.setEndDateTime((String) newEventData.get("endDateTime"));
        event.setDescription((String) newEventData.get("description"));
    }

}
