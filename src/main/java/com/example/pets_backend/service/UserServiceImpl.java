package com.example.pets_backend.service;

import com.example.pets_backend.entity.Event;
import com.example.pets_backend.entity.Task;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        checkUserInDB(user, email);
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), new ArrayList<>());
    }

    @Override
    public User save(User user) {
        String email = user.getEmail();
        if (userRepo.findByEmail(email) != null) {
            log.error("Duplicate email " + email);
            throw new DuplicateKeyException(("Duplicate email " + email));
        } else {
            log.info("Saved new user with email {} into database", email);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User findByUid(String uid) {
        User user = userRepo.findByUid(uid);
        checkUserInDB(user, uid);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void deleteByUid(String uid) {
        User user = userRepo.findByUid(uid);
        checkUserInDB(user, uid);
        userRepo.deleteById(uid);
    }

    @Override
    public void deleteByEmail(String email) {
        User user = userRepo.findByEmail(email);
        checkUserInDB(user, email);
        userRepo.deleteByEmail(email);
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Event getEventByUidAndEventId(String uid, String eventId) {
        User user = userRepo.findByUid(uid);
        checkUserInDB(user, uid);
        Event event = user.getEventByEventId(eventId);
        if (event == null) {
            log.error("Event {} doesn't exist or doesn't belong to User {}", eventId, uid);
            throw new EntityNotFoundException("Event "+eventId+" doesn't exist or doesn't belong to User "+uid);
        } else {
            return event;
        }
    }

    @Override
    public Task getTaskByUidAndTaskId(String uid, String taskId) {
        User user = userRepo.findByUid(uid);
        checkUserInDB(user, uid);
        Task task = user.getTaskByTaskId(taskId);
        if (task == null) {
            log.error("Task {} doesn't exist or doesn't belong to User {}", taskId, uid);
            throw new EntityNotFoundException("Task "+taskId+" doesn't exist or doesn't belong to User "+uid);
        } else {
            return task;
        }
    }

    private void checkUserInDB(User user, String identifier) {
        if (user == null) {
            log.error("User {} not found in database", identifier);
            throw new EntityNotFoundException("User " + identifier + " not found in database");
        } else {
            log.info("User {} found in database", identifier);
        }
    }
}
