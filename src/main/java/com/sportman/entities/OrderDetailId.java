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
public class OrderDetailId {
    @Size(max = 36)
    @NotNull
    @Column(name = "order_id", nullable = false, length = 36)
    private String orderId;

    @Size(max = 36)
    @NotNull
    @Column(name = "product_id", nullable = false, length = 36)
    private String productId;

    @NotNull
    @Column(name = "size_tag", nullable = false)
    private String sizeTag;

}