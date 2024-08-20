package com.sportman.configurations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

    CustomJwtDecoder customJwtDecoder;

    String[] PUBLIC_ENDPOINTS = {
            "/users",
            "/auth/login",
            "/auth/introspect-token",
            "/auth/logout",
            "/auth/refresh"
    };

    String[] PUBLIC_GET_ENDPOINTS = {
            "/clubs",
    };

    String[] PUBLIC_POST_ENDPOINTS = {
            "/clubs/create",
    };

    String[] PUBLIC_DELETE_ENDPOINTS = {
            "/clubs/delete/**",
    };

    String[] PUBLIC_PATCH_ENDPOINTS = {
            "/clubs/update/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        //config public endpoints
        httpSecurity.authorizeHttpRequests(request -> request
                                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.DELETE, PUBLIC_DELETE_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.PATCH, PUBLIC_PATCH_ENDPOINTS).permitAll()
                                .anyRequest()
                                .authenticated()
                        );

        //config protected endpoints
        httpSecurity.oauth2ResourceServer(auth -> auth
                //authenticate
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                )
                .authenticationEntryPoint(new JwtHandlingEntryPoint())
        );


        //turn of crsf
        httpSecurity.csrf(AbstractHttpConfigurer::disable);


        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
