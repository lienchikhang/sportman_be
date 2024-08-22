package com.sportman.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Rates")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rate implements Serializable {
    @EmbeddedId
    RateId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(name = "rate_star")
    Integer rateStar;

    @Lob
    @Column(name = "rate_comment")
    String rateComment;

    @NotNull
    @Column(name = "created_at")
    @CreationTimestamp
    LocalDate createdAt;

}