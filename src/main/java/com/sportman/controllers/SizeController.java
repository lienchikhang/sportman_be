package com.sportman.controllers;

import com.sportman.dto.request.SizeCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.SizeCreateResponse;
import com.sportman.dto.response.page.SizePageResponse;
import com.sportman.services.interfaces.SizeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sizes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizeController {

    SizeService sizeService;

    @GetMapping
    public ApiResponse<SizePageResponse> get(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "pageSize", defaultValue = "6", required = false) int pageSize
    ) {

        Pageable pageable = PageRequest.of(page - 1,pageSize);

        return ApiResponse.<SizePageResponse>builder()
                .statusCode(200)
                .content(sizeService.get(pageable))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<SizeCreateResponse> create(@RequestBody @Valid SizeCreateRequest request) {
        return ApiResponse.<SizeCreateResponse>builder()
                .statusCode(201)
                .content(sizeService.create(request))
                .build();
    }

    @DeleteMapping("/delete/{sizeId}")
    public ApiResponse<Void> delete(@PathVariable(value = "sizeId", required = true) String sizeId) {

        sizeService.delete(sizeId);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .build();
    }

}
