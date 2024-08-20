package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ProductSizeColorId implements Serializable {
    private static final long serialVersionUID = -1431123689920017586L;
    @Size(max = 36)
    @NotNull
    @Column(name = "product_id", nullable = false, length = 36)
    private String productId;

    @Size(max = 6)
    @NotNull
    @Column(name = "color_hex", nullable = false, length = 6)
    private String colorHex;

    @Size(max = 10)
    @NotNull
    @Column(name = "size_tag", nullable = false, length = 10)
    private String sizeTag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductSizeColorId entity = (ProductSizeColorId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.sizeTag, entity.sizeTag) &&
                Objects.equals(this.colorHex, entity.colorHex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, sizeTag, colorHex);
    }

}