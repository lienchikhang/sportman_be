package com.sportman.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Rates")
public class Rate implements Serializable {
    private static final long serialVersionUID = 3871298771950480417L;
    @EmbeddedId
    private RateId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "rate_star")
    private Integer rateStar;

    @Lob
    @Column(name = "rate_comment")
    private String rateComment;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

}