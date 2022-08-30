package com.example.pets_backend;

import com.example.pets_backend.entity.Booking;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.BookingService;
import com.example.pets_backend.service.UserService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class PetsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetsBackendApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/templates");
        configuration.setTemplateLoader(templateLoader);
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        return resolver;
    }

//    @Bean
//    CommandLineRunner bkTest(UserService userService, BookingService bookingService) {
//        return args -> {
//            User user = new User();
//            user.setEmail("1234");
//            user.setPassword("1234456");
//            user.setFirstName("xinyu");
//            user.setLastName("kang");
//            userService.save(user);
//            Booking booking = new Booking(user.getUid(),"12345@qq.com", "a booking", "2020-08-30 22:23", "2020-08-31 10:23", "my home", "something", "pending");
//            booking.setUser(userService.findByUid(user.getUid()));
//            bookingService.save(booking);
//        };
//    }

}
