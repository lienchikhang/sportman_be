package com.sportman.controllers;

import com.sportman.dto.request.RateCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.RateCreateResponse;
import com.sportman.dto.response.page.RatePageResponse;
import com.sportman.services.interfaces.RateService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rates")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RateController {

    RateService rateService;

    @GetMapping("/{productId}")
    public ApiResponse<RatePageResponse> getByProductId(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize,
            @RequestParam(name = "rate", defaultValue = "0") int rate,
            @PathVariable(name = "productId", required = true) String productId
    ) {

        Pageable pageable = PageRequest.of(page -1, pageSize);

        return ApiResponse.<RatePageResponse>builder()
                .statusCode(200)
                .content(rateService.getByProductId(pageable, productId, rate))
                .build();
    }

//    @GetMapping
//    public ApiResponse<RatePageResponse> get(
//            @RequestParam(name = "page", defaultValue = "1") int page,
//            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize
//    ) {
//
//        Pageable pageable = PageRequest.of(page -1, pageSize);
//
//        return ApiResponse.<RatePageResponse>builder()
//                .statusCode(200)
//                .content(rateService.get(pageable))
//                .build();
//    }



    @PostMapping("/create")
    public ApiResponse<RateCreateResponse> create(@RequestBody @Valid RateCreateRequest request) {
        return ApiResponse.<RateCreateResponse>builder()
                .statusCode(201)
                .content(rateService.create(request))
                .build();
    }

}
