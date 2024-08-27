package com.sportman.dto.response.page;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
public abstract class AbstractPageResponse {
    Integer currentPage;
    Long totalPage;
    Long totalElements;
}
