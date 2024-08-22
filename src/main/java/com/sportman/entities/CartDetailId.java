package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetailId implements Serializable {
    @Size(max = 36)
    @NotNull
    @Column(name = "cart_id", nullable = false, length = 36)
    String cartId;

    @Size(max = 36)
    @NotNull
    @Column(name = "product_id", nullable = false, length = 36)
    String productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CartDetailId entity = (CartDetailId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.cartId, entity.cartId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, cartId);
    }

}