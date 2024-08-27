package com.sportman.services.interfaces;


import com.sportman.dto.request.ColorCreateRequest;
import com.sportman.dto.response.ColorCreateResponse;
import com.sportman.dto.response.page.ColorPageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ColorService {

    public ColorCreateResponse create(ColorCreateRequest request);

    public ColorPageResponse get(Pageable pageable);

    public void delete(String colorHex);

}
