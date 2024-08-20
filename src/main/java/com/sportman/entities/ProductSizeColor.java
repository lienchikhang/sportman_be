package com.sportman.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_size_color")
public class ProductSizeColor implements Serializable {
    private static final long serialVersionUID = 1103770038672016672L;
    @EmbeddedId
    private ProductSizeColorId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @MapsId("colorHex")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "color_hex", nullable = false)
    private Color colorHex;

    @MapsId("sizeTag")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "size_tag", nullable = false)
    private Size sizeTag;

}