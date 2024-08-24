package com.sportman.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthLoginResponse {

    String accessToken;

    String refreshToken;

    UserLoginResponse userInfo;

}
