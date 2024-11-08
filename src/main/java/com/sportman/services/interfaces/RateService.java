package com.sportman.services.interfaces;

import com.sportman.dto.request.RateCreateRequest;
import com.sportman.dto.response.RateCreateResponse;
import com.sportman.dto.response.page.RatePageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RateService {

    public RateCreateResponse create(RateCreateRequest request);

    public RatePageResponse getByProductId(Pageable pageable, String productId, int rate);
}
