package com.sportman.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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

    @NotNull
    @Length(min = 8, message = "USERNAME_INVALID")
    String username;

    @NotNull
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotNull
    @Min(value = 6, message = "PASSWORD_INVALID")
    String password;

    @NotNull
    String firstName;

    @NotNull
    String lastName;

}
