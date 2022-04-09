package com.example.pets_backend;

import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PetsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetsBackendApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.register(new User("1044159268@qq.com", "1234", "Xinyu", "Kang"));
            userService.register(new User("1369977889@qq.com", "1234", "Zeyu", "Gong"));
        };
    }

}
