package com.sportman.dto.response.page;

import com.sportman.dto.response.ColorCreateResponse;
import com.sportman.dto.response.SizeCreateResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorPageResponse {

    List<ColorCreateResponse> colors;
    Integer currentPage;
    Long totalPage;
    Long totalElements;

}
