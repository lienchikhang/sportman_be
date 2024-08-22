package com.sportman.dto.request;

import com.sportman.entities.Permission;
import com.sportman.entities.Role;
import com.sportman.entities.RolePermissionId;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionRequest {

    RolePermissionId id;

    Role roleName;

    Permission perName;

}
