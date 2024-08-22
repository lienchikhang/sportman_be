package com.sportman.mappers;

import com.sportman.dto.request.RolePermissionRequest;
import com.sportman.entities.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    @Mappings({
            @Mapping(target = "role", source = "roleName"),
            @Mapping(target = "permission", source = "perName"),
    })
    public RolePermission toRolePermission(RolePermissionRequest request);

}
