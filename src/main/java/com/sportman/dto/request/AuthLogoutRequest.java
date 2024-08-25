package com.sportman.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthLogoutRequest {

    @NotEmpty(message = "TOKEN_NOT_EMPTY")
    String token;

}
