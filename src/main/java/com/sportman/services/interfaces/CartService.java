package com.sportman.services.interfaces;

import com.sportman.dto.request.CartCreateRequest;
import com.sportman.dto.response.page.CartPageResponse;
import org.springframework.data.domain.Pageable;

public interface CartService {

    public CartPageResponse get(Pageable pageable);

    public void add(CartCreateRequest request);

    public void delete(String productId, String sizeTag);

}
