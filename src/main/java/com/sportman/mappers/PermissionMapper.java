package com.sportman.mappers;

import com.sportman.dto.request.PermissionRequest;
import com.sportman.dto.response.PermissionResponse;
import com.sportman.entities.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    public PermissionResponse toResponse(Permission permission);

    public Permission toPermission(PermissionRequest request);

}
