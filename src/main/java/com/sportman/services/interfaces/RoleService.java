package com.sportman.services.interfaces;

import com.sportman.dto.request.RoleRequest;
import com.sportman.dto.request.RoleUpdatingRequest;
import com.sportman.dto.response.RoleResponse;
import com.sportman.enums.EnumRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    public List<RoleResponse> getAll(Pageable pageable);

    public RoleResponse create(RoleRequest request);

    public void delete(String roleId);

    public void updateRole(EnumRole role, RoleUpdatingRequest request);

}
