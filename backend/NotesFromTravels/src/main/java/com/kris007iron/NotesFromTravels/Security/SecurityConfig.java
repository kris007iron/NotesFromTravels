package com.kris007iron.NotesFromTravels.Security;

import com.kris007iron.NotesFromTravels.Repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                        .oauth2Login(oauth2Login -> oauth2Login
                                .successHandler(customAuthenticationSuccessHandler)
                                .failureHandler(authenticationFailureHandler()));
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}
