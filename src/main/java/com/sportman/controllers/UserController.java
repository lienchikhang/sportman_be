package com.sportman.controllers;

import com.sportman.dto.request.AuthRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.AuthResponse;
import com.sportman.services.interfaces.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping("/create")
    public ApiResponse<AuthResponse> create(AuthRequest request) {
        return ApiResponse.<AuthResponse>builder().content(userService.create(request)).build();
    }

}
