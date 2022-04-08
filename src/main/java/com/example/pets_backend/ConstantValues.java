package com.example.pets_backend;


public class ConstantValues {

    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final String SECRET = "secret";
    public static final Long EXPIRATION_TIME_MILLIS = 1000L * 60 * 10;
    public static final Long EXPIRATION_TIME_MILLIS_REFRESH = 1000L * 60 * 60 * 24 * 30;
    public static final String LOGIN = "/login";
    public static final String TOKEN_REFRESH = "/token_refresh";
}
