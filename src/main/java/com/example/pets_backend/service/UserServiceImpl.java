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
            log.info("New user {} saved into to the database", email);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = userRepo.save(user);
        user1.getFolderList().add(new Folder(user1, 0L, "Invoice"));
        user1.getFolderList().add(new Folder(user1, 0L, "Medical Report"));
        user1.getFolderList().add(new Folder(user1, 0L, "Vaccination History"));
        return user1;
    }

    @Override
    public User getById(String uid) {
        User user = userRepo.getById(uid);
        checkUserInDB(user, uid);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void deleteById(String uid) {
        User user = userRepo.getById(uid);
        checkUserInDB(user, uid);
        userRepo.deleteById(uid);
    }

    private void checkUserInDB(User user, String identifier) {
        if (user == null) {
            log.error("User {} not found in the database", identifier);
            throw new UsernameNotFoundException("User " + identifier + " not found in the database");
        } else {
            log.info("User {} found in the database", identifier);
        }
    }
}
