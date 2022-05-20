package com.example.pets_backend.controller;

import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.NtfTaskService;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * This is a temporary controller for the APIs to test the email-sending functionalities
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class EmailController {

    private final NtfTaskService ntfTaskService;
    private final UserService userService;


    @PostMapping("/user/email/task")
    public void sendEmailForTask(@RequestBody Map<String, Object> mapIn) {
        User user = userService.findByUid((String) mapIn.get("uid"));
        boolean isOverdue = (boolean) mapIn.get("isOverdue");
        ntfTaskService.addTasksNotification(user, isOverdue);
    }


//    @PostMapping("/user/email/event")
//    public void sendEmailForEvent(@RequestBody Map<String, Object> mapIn) throws Exception {
//        String email = (String) mapIn.get("email");
//        List<String> petNameList = (List<String>) mapIn.get("petNameList");
//        Map<String, String> templateModel = new HashMap<>();
//        templateModel.put("firstName", (String) mapIn.get("name"));
//        templateModel.put("petNames", String.join(", ", petNameList));
//        templateModel.put("eventTitle", (String) mapIn.get("eventTitle"));
//        templateModel.put("eventStartTime", (String) mapIn.get("eventStartTime"));
//        templateModel.put("eventLocation", (String) mapIn.get("eventLocation"));
//        templateModel.put("petAvatar", (String) mapIn.get("petAvatar"));
//        sendMailService.sendEmail(email, templateModel, TEMPLATE_EVENT);
//    }

}
