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
public class CartDetailId {
    @Size(max = 36)
    @NotNull
    @Column(name = "cart_id", nullable = false, length = 36)
    String cartId;

    @Size(max = 36)
    @NotNull
    @Column(name = "product_id", nullable = false, length = 36)
    String productId;

    @Column(name = "size_tag", nullable = false)
    String sizeTag;
}