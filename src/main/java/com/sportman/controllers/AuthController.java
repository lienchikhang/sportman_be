package com.sportman.controllers;

import com.nimbusds.jose.JOSEException;
import com.sportman.dto.request.*;
import com.sportman.dto.response.*;
import com.sportman.services.interfaces.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
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
    public ApiResponse<AuthIntrospectResponse> introSpect(@RequestHeader("Authorization") String authorization) {

        return ApiResponse.<AuthIntrospectResponse>builder()
                .statusCode(200)
                .content( authService.introspectToken(authorization))
                .build();
    }

    @PostMapping("/introspect-refresh-token")
    public ApiResponse<AuthIntrospectResponse> introSpectRefresh(@RequestBody @Valid AuthIntrospectRequest request) {

        log.info("token: {}", request.getToken());

        return ApiResponse.<AuthIntrospectResponse>builder()
                .statusCode(200)
                .content( authService.introspectRefreshToken(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid AuthLogoutRequest request) throws JOSEException, ParseException {

        authService.logOut(request);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .build();
    }

    @PostMapping("/get-otp")
    public ApiResponse<Void> getOtp(
            @RequestBody AuthOtpRequest request
    ) {

        authService.getOTP(request);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .build();
    }

    @GetMapping("/google/login")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/sportman/oauth2/authorization/google");
    }

    @GetMapping("/google/redirect")
    public void googleLoginRedirect(Principal principal, HttpServletResponse response) throws IOException {
        // Lấy thông tin người dùng từ request
//        Map<String, Object> userAttributes = userDetails;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            log.warn(oauth2User.getName());
            response.sendRedirect("http://localhost:3000/login?user=");
        }


//        log.warn(authentication.getPrincipal());
        // Tạo token (ví dụ: JWT)
//        String token = tokenService.createToken(userAttributes);

        // Chuyển hướng về client với thông tin user và token
        response.sendRedirect("http://localhost:3000/login/failure");
    }
}
