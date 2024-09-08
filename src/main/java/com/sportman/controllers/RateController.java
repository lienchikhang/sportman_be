package com.sportman.controllers;

import com.sportman.dto.request.RateCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.RateCreateResponse;
import com.sportman.services.interfaces.RateService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rates")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RateController {

    RateService rateService;

    @PostMapping("/create")
    public ApiResponse<RateCreateResponse> create(@RequestBody @Valid RateCreateRequest request) {
        return ApiResponse.<RateCreateResponse>builder()
                .statusCode(201)
                .content(rateService.create(request))
                .build();
    }

}
