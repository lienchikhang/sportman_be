package com.sportman.controllers;

import com.sportman.dto.request.ColorCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.ColorCreateResponse;
import com.sportman.dto.response.page.ColorPageResponse;
import com.sportman.mappers.ColorMapper;
import com.sportman.services.interfaces.ColorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/colors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ColorController {

    ColorService colorService;

    @GetMapping
    public ApiResponse<ColorPageResponse> get(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "pageSize", defaultValue = "6", required = false) int pageSize
    ) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return ApiResponse.<ColorPageResponse>builder()
                .statusCode(200)
                .content(colorService.get(pageable))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<ColorCreateResponse> create(
            @RequestBody @Valid ColorCreateRequest request
            ) {
        return ApiResponse.<ColorCreateResponse>builder()
                .statusCode(201)
                .content(colorService.create(request))
                .build();
    }

    @PostMapping("/delete/{colorHex}")
    public ApiResponse<Void> delete(
            @PathVariable(name = "colorHext") String colorHex
    ) {
        colorService.delete(colorHex);
        return ApiResponse.<Void>builder().build();
    }

}
