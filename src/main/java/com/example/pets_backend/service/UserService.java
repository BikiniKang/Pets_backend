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

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.pets_backend.ConstantValues.WEB_PREFIX;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SendMailService sendMailService;
    private final SchedulerService schedulerService;

    /**
     * Load the user by email
     * @param email email
     * @return a Spring in-built User object created by the user's email and password
     * @throws UsernameNotFoundException when email not found or not verified
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!user.isEmail_verified()) {
            throw new UsernameNotFoundException("Email not verified");
        }
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), new ArrayList<>());
    }

    /**
     * Check for duplicate email => encrypt password => store the user into repository
     * @param user User
     * @return user
     */
    public User save(User user) {
        String email = user.getEmail();
        if (userRepo.existsByEmail(email)) {
            throw new DuplicateKeyException(("Duplicate email '" + email + "'"));
        }
        // encrypt the password before stored into repository
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    /**
     * Build a plain text email with the verification link and send to the user's email immediately.
     * Do NOT throw exceptions if the mail-sending job failed
     * @param user user
     */
    public void sendVerifyEmail(User user) {
        String email = user.getEmail();
        String token = user.getVerify_token();  // Get the automatically generated verify token
        // verifyUrl example: https://pets-tracking.azurewebsites.net/#/user/verify?email=petpocket@gmail.com&token=BGkrYVcUe6VWKPKVYCi7k
        String verifyUrl = WEB_PREFIX + "#/user/verify?" +
                "email=" + email + "&" +
                "token=" + token;
        // Plain-text content of the verification email
        String text = "Hi " + user.getFirstName() + ", \n\n" +
                "Click the following link to verify your email: \n" +
                verifyUrl + "\n\n";
        schedulerService.addJobToScheduler(token, () -> {
            try {
                sendMailService.sendVerifyEmail(email, text);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }, LocalDateTime.now());
    }

    /**
     * Find the user by uid, throw exception if user not found
     * @param uid uid
     * @return user
     */
    public User findByUid(String uid) {
        User user = userRepo.findByUid(uid);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    /**
     * Find the User by email (do NOT throw exceptions if not found)
     * @param email email
     * @return the user registered with the email; null if not found
     */
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<User> findAll() {
        log.info("Finding all users in database");
        return userRepo.findAll();
    }

    /**
     * Delete the user by uid, throw exception if user not found
     * @param uid uid
     */
    public void deleteByUid(String uid) {
        if (!userRepo.existsById(uid)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepo.deleteById(uid);
        log.info("User '{}' deleted", uid);
    }

    /**
     * Delete the user by email, throw exception if user not found
     * @param email email
     */
    public void deleteByEmail(String email) {
        if (!userRepo.existsByEmail(email)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepo.deleteByEmail(email);
        log.info("User '{}' deleted", email);
    }

    public Event getEventByUidAndEventId(String uid, String eventId) {
        User user = userRepo.findByUid(uid);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user.getEventByEventId(eventId);
    }

    public Task getTaskByUidAndTaskId(String uid, String taskId) {
        User user = userRepo.findByUid(uid);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user.getTaskByTaskId(taskId);
    }
}
