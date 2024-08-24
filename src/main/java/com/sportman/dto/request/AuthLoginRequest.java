package com.sportman.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthLoginRequest {

    @NotEmpty(message = "USERNAME_NOT_EMPTY")
    String username;

    @NotEmpty(message = "PASSWORD_NOT_EMPTY")
    String password;

}
