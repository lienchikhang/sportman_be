package com.sportman.dto.response.page;

import com.sportman.dto.response.SizeCreateResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizePageResponse extends AbstractPageResponse {
    List<SizeCreateResponse> sizes;
    Integer currentPage;
    Long totalPage;
    Long totalElements;
}
