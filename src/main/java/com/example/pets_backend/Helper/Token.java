package com.example.pets_backend.Helper;

import com.example.pets_backend.Entity.User;

public class Token {

    public static String generateToken(User user) {
        //TODO: modify this method
        return user.getUid() + user.getEmail();
    }

    public static boolean validateToken(String token) {
        //TODO: modify this method
        return true;
    }
}
