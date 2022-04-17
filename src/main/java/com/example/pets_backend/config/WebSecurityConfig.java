package com.example.pets_backend.config;

import com.example.pets_backend.filter.CustomAuthenticationFilter;
import com.example.pets_backend.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.pets_backend.ConstantValues.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        // apply the authentication in our login URL
        customAuthenticationFilter.setFilterProcessesUrl(LOGIN);
        http.cors().and().csrf().disable();
        http.cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // TODO: (temporarily closed the Authorization for all developed requests)
        http.cors().and().authorizeRequests().antMatchers(LOGIN, REGISTER, "/data/**", "/user/**").permitAll();
        // bind all the other URLs
        http.cors().and().authorizeRequests().anyRequest().authenticated();
        http.cors().and().addFilter(customAuthenticationFilter);
        http.cors().and().addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
