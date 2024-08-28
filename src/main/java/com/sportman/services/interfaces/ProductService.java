package com.sportman.services.interfaces;

import com.sportman.dto.request.ProductCreateRequest;
import com.sportman.dto.request.ProductUpdateRequest;
import com.sportman.dto.response.ProductCreateResponse;
import com.sportman.dto.response.page.ProductPageResponse;

public interface ProductService {

    public ProductPageResponse get();

    public ProductCreateResponse create(ProductCreateRequest request);

    public void delete(String productId);

    public ProductCreateResponse update(String productId, ProductUpdateRequest request);


}
