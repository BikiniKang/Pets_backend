package com.example.pets_backend.service;


import com.example.pets_backend.entity.User;

import java.util.List;

public interface UserService {
    User register(User user);
    User getUser(String email);

    List<User> getUsers();
}
