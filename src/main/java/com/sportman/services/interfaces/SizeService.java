package com.sportman.services.interfaces;

import com.sportman.dto.request.SizeCreateRequest;
import com.sportman.dto.response.SizeCreateResponse;
import com.sportman.dto.response.page.SizePageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SizeService {

    public SizeCreateResponse create(SizeCreateRequest request);

    public SizePageResponse get(Pageable pageable);

    public void delete(String sizeId);
}
