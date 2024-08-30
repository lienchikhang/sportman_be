package com.sportman.controllers;

import com.sportman.dto.request.ProductCreateRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.ProductCreateResponse;
import com.sportman.dto.response.page.ProductPageResponse;
import com.sportman.services.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @GetMapping
    public ApiResponse<ProductPageResponse> get() {
        return ApiResponse.<ProductPageResponse>builder().build();
    }

    @PostMapping("/create")
    public ApiResponse<ProductCreateResponse> create(
            @RequestBody @Valid ProductCreateRequest request
            ) {
        return ApiResponse.<ProductCreateResponse>builder()
                .statusCode(201)
                .content(productService.create(request))
                .build();
    }

    @PostMapping("/upload/{productId}")
    public ApiResponse<Void> upload(
            @RequestParam("file") List<MultipartFile> files,
            @PathVariable(name = "productId", required = true) String productId
    ) {

        productService.uploadImage(files, productId);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .msg("upload successfully")
                .build();
    }

}
