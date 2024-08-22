package com.sportman.services.interfaces;

import com.sportman.dto.request.PermissionRequest;
import com.sportman.dto.response.PermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService {

    public List<PermissionResponse> getAll(Pageable pageable);

    public PermissionResponse create(PermissionRequest request);

}
