package com.sportman.entities;

import jakarta.persistence.*;
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
@Table(name = "Orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    String id;

    @Column(name = "created_at")
    LocalDate createdAt;

    @ColumnDefault("0")
    @Column(name = "is_paid")
    Boolean isPaid;

    @ColumnDefault("0")
    @Column(name = "is_delivered")
    Boolean isDelivered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

}