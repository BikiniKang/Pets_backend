package com.example.pets_backend.service;

import com.example.pets_backend.entity.Folder;
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

import java.util.ArrayList;

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
    public User register(User user) {
        String email = user.getEmail();
        if (userRepo.findByEmail(email) != null) {
            log.error("Duplicate email " + email);
            throw new DuplicateKeyException(("Duplicate email " + email));
        } else {
            log.info("Saving new user {} to the database", email);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = userRepo.save(user);
        user1.getFolderList().add(new Folder(user1, 0L, "Invoice"));
        user1.getFolderList().add(new Folder(user1, 0L, "Medical Report"));
        user1.getFolderList().add(new Folder(user1, 0L, "Vaccination History"));
        return user1;
    }

    @Override
    public User getUser(String email) {
        User user = userRepo.findByEmail(email);
        checkUserInDB(user, email);
        return user;
    }

    @Override
    public User getUserById(Long uid) {
        return userRepo.getById(uid);
    }

    @Override
    public User editUser(User user) {
        String email_provided = user.getEmail();
        User user_db = userRepo.findByEmail(email_provided);
        checkUserInDB(user, email_provided);

        // Change the attributes which are editable in the "Setting" page:
        user_db.setFirstName(user.getFirstName());
        user_db.setLastName(user.getLastName());
        user_db.setAddress(user.getAddress());
        user_db.setImage(user.getImage());
        user_db.setPhone(user.getPhone());
        return user_db;
    }

    private void checkUserInDB(User user, String email) {
        if (user == null) {
            log.error("User {} not found in the database", email);
            throw new UsernameNotFoundException("User " + email + " not found in the database");
        } else {
            log.info("User {} found in the database", email);
        }
    }
}
