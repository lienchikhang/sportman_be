package com.sportman.configurations;

import com.sportman.services.imlps.CustomOAuth2UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    @NonFinal
    private CustomJwtDecoder customJwtDecoder;

    private String[] PUBLIC_ENDPOINTS = {
            //GET
            "/clubs",
            "/seasons",
            "/sizes",
            "/colors",
            "/products",
            "/rates",
            "/rates/*",
            "/products/get-list-name",
            "/products/get-by-id/*",
            "/products/get-rates-by-id/*",
            "/auth/google/login",
            "/auth/google/redirect",
            "/vnpay/vn-pay-callback",

            //POST
            "/users/create",
            "/auth/login",
            "/auth/introspect-token",
            "/auth/introspect-refresh-token",
            "/auth/logout",
            "/auth/refresh",
            "/auth/register",
            "/rate/create",
            "/auth/get-otp",

    };



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        //config public endpoints
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .anyRequest()
                .authenticated()
        );

        /**
         * .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
         *             httpSecurityOAuth2LoginConfigurer.loginPage("/sportman/auth/google/login");
         *             httpSecurityOAuth2LoginConfigurer.defaultSuccessUrl("/sportman/auth/google/redirect", true);
         *             httpSecurityOAuth2LoginConfigurer.userInfoEndpoint(userInfoEndpoint ->
         *                             userInfoEndpoint.userService(customOAuth2UserService())
         *                     );
         *         })
         */

        //config protected endpoints
        httpSecurity.oauth2ResourceServer(auth -> auth
                //authenticate
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
                .authenticationEntryPoint(new JwtHandlingEntryPoint())
        );

        //turn off crsf
        httpSecurity.csrf(AbstractHttpConfigurer::disable);


        return httpSecurity.build();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}
