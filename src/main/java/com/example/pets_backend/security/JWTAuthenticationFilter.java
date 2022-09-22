package com.example.pets_backend.security;

import com.example.pets_backend.response.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;

import static com.example.pets_backend.ConstantValues.LOGIN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl(LOGIN);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User scUser = (User) authResult.getPrincipal();
        String access_token = "Bearer " + SecurityHelperMethods.generateAccessToken(request, scUser.getUsername(), scUser.getPassword());
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("token", access_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), ResultData.success(map));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(500);
        new ObjectMapper().writeValue(response.getOutputStream(), ResultData.fail(500, failed.getMessage()));
    }
}
