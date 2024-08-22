package com.sportman.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_size_color")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSizeColor implements Serializable {
    @EmbeddedId
    ProductSizeColorId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @MapsId("colorHex")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "color_hex", nullable = false)
    Color colorHex;

    @MapsId("sizeTag")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "size_tag", nullable = false)
    Size sizeTag;

}