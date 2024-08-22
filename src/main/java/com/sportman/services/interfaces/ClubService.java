package com.sportman.services.interfaces;

import com.sportman.dto.request.ClubRequest;
import com.sportman.dto.request.ClubUpdateRequest;
import com.sportman.dto.response.ClubResponse;
import com.sportman.entities.Club;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubService {

    public List<ClubResponse> getAll(Pageable pageable);

    public ClubResponse create(ClubRequest request);

    public ClubResponse delete(String clubId);

    public void update(String clubId, ClubUpdateRequest request);
}
