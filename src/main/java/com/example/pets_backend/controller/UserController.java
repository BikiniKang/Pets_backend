package com.example.pets_backend.controller;

import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
        userService.deleteByUid((String) mapIn.get("uid"));
    }

    @PostMapping("/user/all")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/user/dashboard")
    public LinkedHashMap<String, Object> getUserDashboard(@RequestBody Map<String, Object> mapIn) {
        User user = userService.findByUid((String) mapIn.get("uid"));
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("firstName", user.getFirstName());
        mapOut.put("lastName", user.getLastName());
        mapOut.put("image", user.getImage());
        mapOut.put("petList", user.getPetAbList());
        mapOut.put("eventList", user.getEventAbList());
        mapOut.put("taskList", user.getTaskAbList());
        return mapOut;
    }

    @PostMapping("/user/profile")
    public LinkedHashMap<String, Object> getUserProfile(@RequestBody Map<String, Object> mapIn) {
        User user = userService.findByUid((String) mapIn.get("uid"));
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
        User user = userService.findByUid((String) mapIn.get("uid"));
        user.setFirstName((String) mapIn.get("firstName"));
        user.setLastName((String) mapIn.get("lastName"));
        user.setPhone((String) mapIn.get("phone"));
        user.setAddress((String) mapIn.get("address"));
        user.setPetSitter((boolean) mapIn.get("isPetSitter"));
    }

    @PostMapping("/user/profile/image/update")
    @Transactional
    public void updateUserProfileImage(@RequestBody Map<String, Object> mapIn) {
        User user = userService.findByUid((String) mapIn.get("uid"));
        user.setImage((String) mapIn.get("image"));
    }
}
