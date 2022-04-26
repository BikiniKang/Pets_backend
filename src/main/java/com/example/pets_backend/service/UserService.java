package com.example.pets_backend.service;

import com.example.pets_backend.entity.User;

import java.util.List;

public interface UserService {
    User save(User user);
    User getById(String uid);
    List<User> findAll();
    void deleteById(String uid);
    User findByEmail(String email);
}
