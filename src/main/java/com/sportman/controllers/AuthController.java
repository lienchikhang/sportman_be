package com.sportman.controllers;

import com.nimbusds.jose.JOSEException;
import com.sportman.dto.request.AuthIntrospectRequest;
import com.sportman.dto.request.AuthLoginRequest;
import com.sportman.dto.request.AuthLogoutRequest;
import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.*;
import com.sportman.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest request) {
        return ApiResponse.<AuthLoginResponse>builder()
                .statusCode(200)
                .content(authService.logIn(request))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthRefreshResponse> refresh(@RequestBody @Valid AuthIntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthRefreshResponse>builder()
                .statusCode(200)
                .content(authService.refresh(request))
                .build();
    }

    @PostMapping("/introspect-token")
    public ApiResponse<AuthIntrospectResponse> introSpect(@RequestBody @Valid AuthIntrospectRequest request) {

        return ApiResponse.<AuthIntrospectResponse>builder()
                .statusCode(200)
                .content( authService.introspectToken(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid AuthLogoutRequest request) throws JOSEException, ParseException {

        authService.logOut(request);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .build();
    }

}
