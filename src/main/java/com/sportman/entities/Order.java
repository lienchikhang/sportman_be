package com.sportman.entities;

import jakarta.persistence.*;
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
@Table(name = "Orders")
public class Order implements Serializable {
    private static final long serialVersionUID = -299071375338755741L;
    @Id
    @Size(max = 36)
    @ColumnDefault("(uuid())")
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ColumnDefault("0")
    @Column(name = "is_paid")
    private Boolean isPaid;

    @ColumnDefault("0")
    @Column(name = "is_delivered")
    private Boolean isDelivered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}