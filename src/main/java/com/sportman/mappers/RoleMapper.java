package com.sportman.mappers;

import com.sportman.dto.request.RoleRequest;
import com.sportman.dto.response.RoleResponse;
import com.sportman.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "rolePermissions", ignore = true)
    public Role toRole(RoleRequest request);

    public RoleResponse toRoleResponse(Role role);

}
