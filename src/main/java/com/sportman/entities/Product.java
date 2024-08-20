package com.sportman.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Products")
public class Product implements Serializable {
    private static final long serialVersionUID = -1602042705427065461L;
    @Id
    @Size(max = 36)
    @ColumnDefault("(uuid())")
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Lob
    @Column(name = "product_desc", nullable = false)
    private String productDesc;

    @NotNull
    @Column(name = "product_price", nullable = false)
    private Integer productPrice;

    @NotNull
    @Lob
    @Column(name = "front_image", nullable = false)
    private String frontImage;

    @NotNull
    @Lob
    @Column(name = "back_image", nullable = false)
    private String backImage;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Season seasons;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_name")
    private Club clubName;

}