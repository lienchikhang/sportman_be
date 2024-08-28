package com.sportman.dto.response;

import com.sportman.entities.AbstractEntity;
import com.sportman.entities.Club;
import com.sportman.entities.ProductSize;
import com.sportman.entities.Season;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateResponse {
    String id;
    String productName;
    String productDesc;
    Integer productPrice;
    String frontImage;
    String backImage;
    Season season;
    Club club;
    List<ProductSize> stocks;

}