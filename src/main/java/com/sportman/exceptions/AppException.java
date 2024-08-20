package com.sportman.exceptions;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppException extends RuntimeException {

    ErrorCode errorCode;

}
