package com.sportman.services.interfaces;

import com.sportman.dto.request.SeasonCreateRequest;
import com.sportman.dto.response.SeasonResponse;
import com.sportman.dto.response.page.SeasonPageResponse;
import com.sportman.entities.Season;
import com.sportman.entities.SeasonId;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeasonService {

    public SeasonPageResponse getAll(Pageable pageable);

    public SeasonResponse create(SeasonCreateRequest request);

    public void delete(SeasonCreateRequest request);
}
