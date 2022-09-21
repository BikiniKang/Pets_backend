package com.example.pets_backend.service;

import com.example.pets_backend.entity.Event;
import com.example.pets_backend.entity.Task;
import com.example.pets_backend.entity.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
    User save(User user);
    void sendVerifyEmail(User user) throws MessagingException;
    void deleteByUid(String uid);
    void deleteByEmail(String email);
    User findByUid(String uid);
    User findByEmail(String email);
    List<User> findAll();

    Event getEventByUidAndEventId(String uid, String eventId);
    Task getTaskByUidAndTaskId(String uid, String taskId);
}
