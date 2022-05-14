package com.example.pets_backend.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.pets_backend.entity.Task;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.TaskService;
import com.example.pets_backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class TaskController {

    private final UserService userService;
    private final TaskService taskService;
    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/user/task/add")
    public Map<String, Object> addTask(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        User user = userService.findByUid(uid);
        Task task = mapper.convertValue(mapIn.get("taskData"), Task.class);

        List<String> petIdList = user.getPetIdList();
        if (!petIdList.containsAll(task.getPetIdList())) {
            throw new IllegalArgumentException("One or more petIds do not belong to User '" + uid + "'");
        }
        task.setTaskId(NanoIdUtils.randomNanoId());
        task.setUser(user);
        taskService.save(task);

        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("task", task);
        return mapOut;
    }

    @DeleteMapping("/user/task/delete")
    public void deleteTask(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        String taskId = (String) mapIn.get("taskId");
        userService.getTaskByUidAndTaskId(uid, taskId);
        taskService.deleteByTaskId(taskId);
    }

    @PostMapping("/user/task/edit")
    @Transactional
    public Map<String, Object> editTask(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        Task taskNew = mapper.convertValue(mapIn.get("taskData"), Task.class);
        String taskId = taskNew.getTaskId();
        Task task = userService.getTaskByUidAndTaskId(uid, taskId);

        // update all attributes except taskId, user
        task.setTaskTitle(taskNew.getTaskTitle());
        task.setPetIdList(taskNew.getPetIdList());
        task.setChecked(taskNew.isChecked());
        task.setDueDate(taskNew.getDueDate());

        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("task", task);
        return mapOut;
    }

    @PostMapping("/user/task/check")
    @Transactional
    public Map<String, Object> checkTask(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        String taskId = (String) mapIn.get("taskId");
        Task task = userService.getTaskByUidAndTaskId(uid, taskId);

        task.setChecked((int) mapIn.get("isChecked") != 0);

        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("task", task);
        return mapOut;
    }

    @PostMapping("/user/task/date")
    public Map<String, Object> getTasksByDate(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        User user = userService.findByUid(uid);
        String date = (String) mapIn.get("date");

        List<Task> taskList = user.getTasksByDate(date);

        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("uid", uid);
        mapOut.put("taskList", taskList);
        return mapOut;
    }

    @PostMapping("/user/task")
    public Task getTask(@RequestBody Map<String, Object> mapIn) {
        String taskId = (String) mapIn.get("taskId");
        return taskService.findByTaskId(taskId);
    }

    @PostMapping("/user/task/all")
    public List<Task> getAllTasks(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        User user = userService.findByUid(uid);
        return user.getTaskList();
    }

    @PostMapping("/user/task/overdue/all")
    public List<Task> getOverdueTasks(@RequestBody Map<String, Object> mapIn) {
        String uid = (String) mapIn.get("uid");
        User user = userService.findByUid(uid);
        return user.getOverdueTasks(LocalDate.now().toString());
    }
}
