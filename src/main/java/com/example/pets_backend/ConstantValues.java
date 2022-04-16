package com.example.pets_backend;


import com.auth0.jwt.algorithms.Algorithm;

import java.nio.charset.StandardCharsets;

public class ConstantValues {

    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final String SECRET = "secret";
    public static final Long EXPIRATION_TIME_MILLIS = 1000L * 60 * 120;
    public static final Long EXPIRATION_TIME_MILLIS_REFRESH = 1000L * 60 * 60 * 24 * 30;
    public static final String LOGIN = "/login";
    public static final String TOKEN_REFRESH = "/token_refresh";
    public static final String REGISTER = "/register";
    public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
    public static final String DEFAULT_IMAGE = "http://dummyimage.com/400x400";
    public static final String DEFAULT_IMAGE_PET = "http://dummyimage.com/400x400";
    public static final String DEFAULT_ADDRESS = "";
    public static final String DEFAULT_PHONE = "";
    public static final String DEFAULT_EVENT_TYPE = "";
    public static final String DEFAULT_START_TIME = "";
    public static final String DEFAULT_END_TIME = "";
}
