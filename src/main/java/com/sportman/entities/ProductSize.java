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
@Table(name = "product_size")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSize {
    @EmbeddedId
    ProductSizeId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @MapsId("sizeTag")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "size_tag", nullable = false)
    Size sizeTag;

    @Column(name = "stock", nullable = false)
    Integer stock;
}