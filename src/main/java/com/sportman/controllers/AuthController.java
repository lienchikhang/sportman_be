package com.sportman.controllers;

import com.sportman.dto.request.AuthLoginRequest;
import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.AuthLoginResponse;
import com.sportman.dto.response.AuthResponse;
import com.sportman.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@RequestBody @Valid AuthRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .statusCode(201)
                .content(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest request) {
        return ApiResponse.<AuthLoginResponse>builder()
                .statusCode(200)
                .content(authService.logIn(request))
                .build();
    }

}
