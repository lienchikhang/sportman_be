package com.sportman.services.interfaces;

import com.sportman.dto.request.RateCreateRequest;
import com.sportman.dto.response.RateCreateResponse;

public interface RateService {

    public RateCreateResponse create(RateCreateRequest request);


}
