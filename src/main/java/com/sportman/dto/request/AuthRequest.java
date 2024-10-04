package com.sportman.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequest {

    @NotEmpty(message = "USERNAME_NOT_EMPTY")
    @Length(min = 8, message = "USERNAME_INVALID")
    String username;

    @NotEmpty(message = "")
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotEmpty(message = "PASSWORD_NOT_EMPTY")
    @Min(value = 6, message = "PASSWORD_INVALID")
    String password;

    @NotEmpty(message = "")
    String firstName;

    @NotEmpty(message = "")
    String lastName;

    @NotEmpty(message = "OTP_NOT_EMPTY")
    String otp;

}
