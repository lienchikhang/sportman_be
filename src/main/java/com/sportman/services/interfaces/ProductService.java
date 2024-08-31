package com.sportman.services.interfaces;

import com.sportman.dto.request.ProductCreateRequest;
import com.sportman.dto.request.ProductUpdateRequest;
import com.sportman.dto.request.ProductUpdateStockRequest;
import com.sportman.dto.response.ProductCreateResponse;
import com.sportman.dto.response.page.ProductPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    public ProductPageResponse get(Pageable pageable,
                                   String name,
                                   String club,
                                   String season,
                                   Integer price,
                                   String sizes,
                                   Boolean isDeleted,
                                   String sort);

    public ProductCreateResponse create(ProductCreateRequest request);

    public void delete(String productId);

    public ProductCreateResponse update(String productId, ProductUpdateRequest request);

    public void uploadImage(List<MultipartFile> file, String productId);

    public void updateStock(String productId, String sizeTag, ProductUpdateStockRequest request);

    public ProductCreateResponse getById(String productId);
}
