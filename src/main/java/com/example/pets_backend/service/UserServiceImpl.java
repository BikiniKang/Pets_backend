package com.example.pets_backend.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
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

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.example.pets_backend.ConstantValues.WEB_PREFIX;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SendMailService sendMailService;

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
            log.error("Duplicate email '" + email + "'");
            throw new DuplicateKeyException(("Duplicate email '" + email + "'"));
        } else {
            log.info("New user '{}' saved into database", user.getUid());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Transactional
    @Override
    public void sendVerifyEmail(User user) throws MessagingException {
        String email = user.getEmail();
        String token = NanoIdUtils.randomNanoId();
        user.setVerify_token(token);
        String text = "Hi " + user.getFirstName() + ", \n\n" +
                "Click the following link to verify your email: \n" +
                WEB_PREFIX + "#/user/verify?token=" + token + "\n\n";
        sendMailService.sendVerifyEmail(email, text);
    }

    @Override
    public User findByUid(String uid) {
        User user = userRepo.findByUid(uid);
        checkUserInDB(user, uid);
        return user;
    }

    @Override
    public List<User> findAll() {
        log.info("Finding all users in database");
        return userRepo.findAll();
    }

    @Override
    public void deleteByUid(String uid) {
        User user = userRepo.findByUid(uid);
        checkUserInDB(user, uid);
        userRepo.deleteById(uid);
        log.info("User '{}' deleted from database", uid);
    }

    @Override
    public void deleteByEmail(String email) {
        User user = userRepo.findByEmail(email);
        checkUserInDB(user, email);
        userRepo.deleteByEmail(email);
        log.info("User with email '{}' deleted from database", email);
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepo.findByEmail(email);
//        checkUserInDB(user, email);
        return user;
    }

    @Override
    public Event getEventByUidAndEventId(String uid, String eventId) {
        User user = userRepo.findByUid(uid);
        checkUserInDB(user, uid);
        return user.getEventByEventId(eventId);
    }

    @Override
    public Task getTaskByUidAndTaskId(String uid, String taskId) {
        User user = userRepo.findByUid(uid);
        checkUserInDB(user, uid);
        return user.getTaskByTaskId(taskId);
    }

    private void checkUserInDB(User user, String identifier) {
        if (user == null) {
            log.error("User '{}' not found in database", identifier);
            throw new EntityNotFoundException("User '" + identifier + "' not found in database");
        } else {
            log.info("User '{}' found in database", identifier);
        }
    }
}
