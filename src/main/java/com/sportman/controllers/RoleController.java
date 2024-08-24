package com.sportman.controllers;

import com.sportman.dto.request.RoleRequest;
import com.sportman.dto.request.RoleUpdatingRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.RoleResponse;
import com.sportman.enums.EnumRole;
import com.sportman.services.interfaces.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name ="pageSize", defaultValue = "4")  int pageSize
            ) {

        Pageable pageable = PageRequest.of(page, pageSize);

        return ApiResponse.<List<RoleResponse>>builder()
                .statusCode(200)
                .content(roleService.getAll(pageable))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<RoleResponse> getAll(
            @RequestBody @Valid RoleRequest request
            ) {

        return ApiResponse.<RoleResponse>builder()
                .statusCode(200)
                .content(roleService.create(request))
                .build();
    }

    @PatchMapping("/update/{roleId}")
    public ApiResponse<Void> update(
            @PathVariable("roleId") EnumRole roleId,
            @RequestBody @Valid RoleUpdatingRequest request
    ) {

        roleService.updateRole(roleId, request);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .build();
    }


}
