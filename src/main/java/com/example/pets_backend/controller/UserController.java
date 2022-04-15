package com.example.pets_backend.controller;

import com.example.pets_backend.entity.CalendarDate;
import com.example.pets_backend.entity.Folder;
import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping(REGISTER)
    public LinkedHashMap<String, Object> register(@RequestBody Map<String, Object> mapIn) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        User user = new User((String) mapIn.get("email"), (String) mapIn.get("password"), (String) mapIn.get("firstName"), (String) mapIn.get("lastName"));
        user.setImage(DEFAULT_IMAGE);
        user.setAddress("");
        user.setPhone("");
        map.put("uid", userService.register(user).getUid().intValue());
        return map;
    }

    @GetMapping("/user/dashboard")
    public LinkedHashMap<String, Object> getUserDashboard(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getUserById((long) ((int) mapIn.get("uid")));
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("firstName", user.getFirstName());
        mapOut.put("lastName", user.getLastName());
        mapOut.put("image", user.getImage());

        ArrayList<LinkedHashMap<String, Object>> petList = new ArrayList<>();
        for (Pet pet : user.getPetList()) {
            LinkedHashMap<String, Object> map_p = new LinkedHashMap<>();
            map_p.put("petId", pet.getPetId());
            map_p.put("petName", pet.getPetName());
            map_p.put("petImg", pet.getPetImg());
            petList.add(map_p);
        }
        mapOut.put("petList", petList);

        ArrayList<LinkedHashMap<String, Object>> folderList = new ArrayList<>();
        for (Folder folder : user.getFolderList()) {
            LinkedHashMap<String, Object> map_f = new LinkedHashMap<>();
            map_f.put("folderId", folder.getFolderId());
            map_f.put("folderName", folder.getFolderName());
            folderList.add(map_f);
        }
        mapOut.put("folderList", folderList);

        LinkedHashMap<String, Object> map_c = new LinkedHashMap<>();
        if (user.getCalendarDateList().size() != 0) {
            CalendarDate calendarDate = user.getCalendarDateList().get(0); // TODO: need to get today's calendarDate object
            map_c.put("calDateId", calendarDate.getCalDateId());
            map_c.put("dataValue", calendarDate.getDateValue());
            map_c.put("taskList", calendarDate.getTaskListSub());
            map_c.put("eventList", calendarDate.getEventListSub());
            mapOut.put("calendarDate", map_c);
        } else {
            mapOut.put("calendarDate", null);
        }
        return mapOut;
    }

    @GetMapping("/user/profile")
    public LinkedHashMap<String, Object> getUserProfile(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getUserById((long) ((int) mapIn.get("uid")));
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("firstName", user.getFirstName());
        mapOut.put("lastName", user.getLastName());
        mapOut.put("image", user.getImage());
        mapOut.put("email", user.getEmail());
        mapOut.put("phone", user.getPhone());
        mapOut.put("address", user.getAddress());
        mapOut.put("isPetSitter", user.isPetSitter());
        mapOut.put("petList", user.getPetListSub());
        return mapOut;
    }

    @PutMapping("/user/profile")
    @Transactional
    public void updateUserProfile(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getUserById((long) ((int) mapIn.get("uid")));
        user.setFirstName((String) mapIn.get("firstName"));
        user.setLastName((String) mapIn.get("lastName"));
        user.setPhone((String) mapIn.get("phone"));
        user.setAddress((String) mapIn.get("address"));
        user.setPetSitter((boolean) mapIn.get("isPetSitter"));
    }

    @PutMapping("/user/profile/image")
    @Transactional
    public void updateUserProfileImage(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getUserById((long) ((int) mapIn.get("uid")));
        user.setImage((String) mapIn.get("image"));
    }

}
