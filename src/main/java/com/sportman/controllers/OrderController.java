package com.sportman.controllers;

import com.cloudinary.Api;
import com.sportman.dto.request.OrderCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.OrderResponse;
import com.sportman.dto.response.page.OrderPageResponse;
import com.sportman.enums.OrderStatus;
import com.sportman.services.interfaces.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(name = "sort", defaultValue = "desc") String sort,
            @RequestParam(name = "status", required = false) OrderStatus status
    ) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return ApiResponse.<OrderPageResponse>builder()
                .statusCode(200)
                .content(orderService.getAll(pageable, sort, status))
                .build();
    }

    @GetMapping("/get-by-user")
    public ApiResponse<OrderPageResponse> getAllByUser(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            @RequestParam(name = "status", defaultValue = "UNPAID") OrderStatus status
    ) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return ApiResponse.<OrderPageResponse>builder()
                .statusCode(200)
                .content(orderService.getAllByUser(pageable, sort))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<OrderResponse> create(
            @RequestBody @Valid OrderCreateRequest request
    ) {
        return ApiResponse.<OrderResponse>builder()
                .statusCode(201)
                .content(orderService.create(request))
                .build();
    }

    @DeleteMapping("/cancel/{orderId}")
    public ApiResponse<OrderResponse> cancel(
            @PathVariable(name = "orderId") String orderId
    ) {
        return ApiResponse.<OrderResponse>builder()
                .statusCode(200)
                .content(orderService.cancel(orderId))
                .build();
    }

    @PatchMapping("/confirm/{orderId}")
    public ApiResponse<OrderResponse> confirm(
            @PathVariable(name = "orderId") String orderId
    ) {
        return ApiResponse.<OrderResponse>builder()
                .statusCode(200)
                .content(orderService.confirmPaid(orderId))
                .build();
    }

}
