package com.sportman.controllers;

import com.sportman.dto.request.ProductCreateRequest;
import com.sportman.dto.request.ProductUpdateRequest;
import com.sportman.dto.request.ProductUpdateStockRequest;
import com.sportman.dto.response.ApiResponse;
import com.sportman.dto.response.ProductCreateResponse;
import com.sportman.dto.response.ProductGetDetailResponse;
import com.sportman.dto.response.ProductNameResponse;
import com.sportman.dto.response.page.ProductPageResponse;
import com.sportman.dto.response.page.RatePageResponse;
import com.sportman.entities.Product;
import com.sportman.services.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @GetMapping
    public ApiResponse<ProductPageResponse> get(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "12") int pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "club", required = false) String club,
            @RequestParam(name = "season", required = false) String season,
            @RequestParam(name = "price", required = false) Integer price,
            @RequestParam(name = "size", required = false) String sizes,
            @RequestParam(name = "sort", required = false) String sort
            ) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return ApiResponse.<ProductPageResponse>builder()
                .statusCode(200)
                .content(productService
                        .get(pageable, name, club, season, price, sizes, false, sort))
                .build();
    }

    @GetMapping("/get-list-name")
    public ApiResponse<ProductPageResponse> getOnlyName(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize
    ) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return ApiResponse.<ProductPageResponse>builder()
                .statusCode(200)
                .content(productService.getListName(pageable, name))
                .build();
    }

    @GetMapping("/get-by-id/{productId}")
    public ApiResponse<ProductGetDetailResponse> getById(
            @PathVariable(name = "productId") String productId
    ) {
        return ApiResponse.<ProductGetDetailResponse>builder()
                .content(productService.getById(productId))
                .statusCode(200)
                .build();
    }

    @GetMapping("/get-rates-by-id/{productId}")
    public ApiResponse<RatePageResponse> getRatesById(
            @PathVariable(name = "productId", required = true) String productId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize
    ) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        return ApiResponse.<RatePageResponse>builder()
                .content(productService.getRatesByProductId(productId, pageable))
                .build();
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

    @PatchMapping("/update/{productId}")
    public ApiResponse<ProductCreateResponse> update(
            @RequestBody @Valid ProductUpdateRequest request,
            @PathVariable(name = "productId") String productId
    ) {
        return ApiResponse.<ProductCreateResponse>builder()
                .statusCode(200)
                .content(productService.update(productId, request))
                .msg("update successfully")
                .build();
    }

    @PatchMapping("/update-stock/{productId}/{sizeId}")
    public ApiResponse<Void> updateStock(
            @RequestBody @Valid ProductUpdateStockRequest request,
            @PathVariable(name = "productId") String productId,
            @PathVariable(name = "sizeId") String sizeId
    ) {
        productService.updateStock(productId,sizeId, request);
        return ApiResponse.<Void>builder()
                .statusCode(200)
                .msg("update successfully")
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
