package com.sportman.services.imlps;

import com.sportman.dto.request.PermissionRequest;
import com.sportman.dto.response.PermissionResponse;
import com.sportman.entities.Permission;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.PermissionMapper;
import com.sportman.repositories.PermissionRepository;
import com.sportman.services.interfaces.PermissionService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public List<PermissionResponse> getAll(Pageable pageable) {
        return permissionRepository
                .findAll(pageable)
                .stream()
                .map(permissionMapper::toResponse).toList();
    }

    @Override
    public PermissionResponse create(PermissionRequest request) {

        if (permissionRepository.existsById(request.getName())) throw new AppException(ErrorCode.PERMISSION_EXISTED);

        return permissionMapper.toResponse(
                permissionRepository.save(
                        permissionMapper.toPermission(request)
                )
        );
    }
}
