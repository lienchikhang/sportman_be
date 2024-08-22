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
public class RateId implements Serializable {
    private static final long serialVersionUID = -6044100120554083446L;
    @Size(max = 36)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 36, unique = true)
    private String userId;

    @Size(max = 36)
    @NotNull
    @Column(name = "product_id", nullable = false, length = 36, unique = true)
    private String productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RateId entity = (RateId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userId);
    }

}