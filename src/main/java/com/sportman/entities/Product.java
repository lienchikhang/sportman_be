package com.sportman.entities;

import com.sportman.enums.ProductLeague;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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
    @Column(name = "front_image", nullable = true)
    String frontImage;

    @NotNull
    @Lob
    @Column(name = "back_image", nullable = true)
    String backImage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "league")
    ProductLeague league;

    @Column(name = "sum_rate")
    Double sumRate;

    @ManyToOne
    Season season;

    @ManyToOne
    @JoinColumn(name = "club_name")
    Club club;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ProductSize> stocks;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Color> colors;

}