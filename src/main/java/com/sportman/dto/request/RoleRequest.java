package com.sportman.dto.request;

import com.sportman.enums.EnumRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    @NotNull
    EnumRole name;

    @NotNull
    String roleDesc;

    List<String> permissions;

}
