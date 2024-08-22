package com.sportman.controllers;

import com.sportman.dto.request.PermissionRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.PermissionResponse;
import com.sportman.services.interfaces.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "4") int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);

        return ApiResponse.<List<PermissionResponse>>builder()
                .statusCode(200)
                .content(permissionService.getAll(pageable))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<PermissionResponse> getAll(
            @RequestBody @Valid PermissionRequest request
            ) {
        return ApiResponse.<PermissionResponse>builder()
                .statusCode(201)
                .content(permissionService.create(request))
                .build();
    }

}
