package com.example.pets_backend.listener;


import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.persistence.PostUpdate;

@RequiredArgsConstructor
public class UserListener {

    private final UserService userService;

    @PostUpdate
    private void updateUserName() {
        // update the username in notifications
    }
}
