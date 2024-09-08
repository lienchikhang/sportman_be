package com.sportman.services.imlps;

import com.sportman.dto.request.RolePermissionRequest;
import com.sportman.dto.request.RoleRequest;
import com.sportman.dto.request.RoleUpdatingRequest;
import com.sportman.dto.response.RoleResponse;
import com.sportman.entities.Permission;
import com.sportman.entities.Role;
import com.sportman.entities.RolePermission;
import com.sportman.entities.RolePermissionId;
import com.sportman.enums.EnumRole;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.mappers.RoleMapper;
import com.sportman.mappers.RolePermissionMapper;
import com.sportman.repositories.PermissionRepository;
import com.sportman.repositories.RolePermissionRepository;
import com.sportman.repositories.RoleRepository;
import com.sportman.services.interfaces.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;
    RolePermissionRepository rolePermissionRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll(Pageable pageable) {
        return roleRepository
                .findAll(pageable)
                .stream()
                .map(role -> roleMapper.toRoleResponse(role))
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('CREATE_ROLE') || hasRole('ADMIN')")
    @Transactional
    public RoleResponse create(RoleRequest request) {

        //check role exist
        if (roleRepository.existsById(request.getName())) throw new AppException(ErrorCode.ROLE_EXISTED);

        //check permission
        if (CollectionUtils.isEmpty(request.getPermissions())) throw new AppException(ErrorCode.ROLE_INVALID);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

        //create new role
        Role newRole = roleMapper.toRole(request);
        newRole.setRolePermissions(new HashSet<>(permissions
                                .stream()
                                .map(per -> rolePermissionMapper
                                        .toRolePermission(
                                                //create rolePermission per permission
                                                RolePermissionRequest
                                                .builder()
                                                .perName(per)
                                                .roleName(newRole)
                                                .id(RolePermissionId
                                                        .builder()
                                                        .perName(per.getName())
                                                        .roleName(newRole.getName())
                                                        .build())
                                                .build()
                                        ))
                                .toList()
        ));

//        newRole.getRolePermissions().forEach(rolePermission -> rolePermission.setRole(newRole));

        return roleMapper.toRoleResponse(roleRepository.save(newRole));
    }

    @Override
    public void delete(String roleId) {

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateRole(EnumRole role, RoleUpdatingRequest request) {

        //check role exist
        Role existedRole = roleRepository.findById(role).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

        existedRole.setRolePermissions(new HashSet<>(
                permissions
                        .stream()
                        .map(permission -> rolePermissionMapper
                                .toRolePermission(RolePermissionRequest
                                        .builder()
                                        .roleName(existedRole)
                                        .perName(permission)
                                        .id(RolePermissionId
                                                .builder()
                                                .roleName(existedRole.getName())
                                                .perName(permission.getName())
                                                .build())
                                        .build())
                        ).toList()
                )
        );

        existedRole.getRolePermissions().forEach(rolePermission -> rolePermission.setRole(existedRole));

        roleRepository.save(existedRole);
    }
}
