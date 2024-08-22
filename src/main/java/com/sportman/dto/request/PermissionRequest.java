package com.sportman.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {

    @NotEmpty(message = "PER_NAME_NOT_EMPTY")
    String name;

    @NotEmpty(message = "PER_DESC_NOT_EMPTY")
    String perDesc;

}
