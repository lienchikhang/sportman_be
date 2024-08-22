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
@Table(name = "CartDetails")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetail implements Serializable {
    @EmbeddedId
    CartDetailId id;

    @MapsId("cartId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(name = "amount")
    Integer amount;

}