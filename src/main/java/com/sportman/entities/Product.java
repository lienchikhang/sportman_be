package com.sportman.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "product_name", nullable = false, unique = true)
    String productName;

    @NotNull
    @Lob
    @Column(name = "product_desc", nullable = false)
    String productDesc;

    @NotNull
    @Column(name = "product_price", nullable = false)
    Integer productPrice;

    @NotNull
    @Lob
    @Column(name = "front_image", nullable = false)
    String frontImage;

    @NotNull
    @Lob
    @Column(name = "back_image", nullable = false)
    String backImage;

    @ManyToOne(fetch = FetchType.LAZY)
    Season seasons;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_name")
    Club clubName;

}