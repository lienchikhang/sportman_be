package com.sportman.controllers;

import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.page.OrderPageResponse;
import com.sportman.services.interfaces.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderController {

    OrderService orderService;

    @GetMapping
    public ApiResponse<OrderPageResponse> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize,
            @RequestParam(name = "sort", defaultValue = "asc") String sort
    ) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return ApiResponse.<OrderPageResponse>builder()
                .statusCode(200)
                .content(orderService.getAll(pageable, sort))
                .build();
    }

}
