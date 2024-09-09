package com.sportman.controllers;

import com.sportman.dto.request.CartCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.page.CartPageResponse;
import com.sportman.services.interfaces.CartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartController {

    CartService cartService;

    @GetMapping
    public ApiResponse<CartPageResponse> get(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize
    ) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return ApiResponse.<CartPageResponse>builder()
                .statusCode(200)
                .content(cartService.get(pageable))
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<Void> add(@RequestBody @Valid CartCreateRequest request) {
        cartService.add(request);
        return ApiResponse.<Void>builder()
                .statusCode(200)
                .msg("Add successfully")
                .build();
    }

    @DeleteMapping("/delete/{productId}/{sizeTag}")
    public ApiResponse<Void> delete(
            @PathVariable(name = "productId") String productId,
            @PathVariable(name = "sizeTag") String sizeTag
    ) {
        cartService.delete(productId, sizeTag);
        return ApiResponse.<Void>builder()
                .statusCode(200)
                .msg("Delete successfully")
                .build();
    }

}
