package com.example.pets_backend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.pets_backend.entity.CalendarDate;
import com.example.pets_backend.entity.Folder;
import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping(REGISTER)
    public LinkedHashMap<String, Object> register(@RequestBody Map<String, Object> mapIn) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        User user = new User((String) mapIn.get("email"), (String) mapIn.get("password"), (String) mapIn.get("firstName"), (String) mapIn.get("lastName"));
        map.put("uid", userService.register(user).getUid().intValue());
        return map;
    }

    @GetMapping("/user/dashboard")
    public LinkedHashMap<String, Object> getUserDashboard(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getUserById((long) ((int) mapIn.get("uid")));
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("uid", user.getUid());
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
        }
        mapOut.put("calendarDate", map_c);
        return mapOut;
    }

    @PutMapping("/user/edit_setting")
    public User editUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON_VALUE);

        // check whether the provided user matches the current user
        String email_provided = user.getEmail();
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length()); // no need to check the token format because the CustomAuthorizationFilter already did that
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        if (!email_provided.equals(decodedJWT.getSubject())) {
            throw new IllegalArgumentException("Do not have access to edit user " + email_provided);
        }

        return userService.editUser(user);
    }

}
