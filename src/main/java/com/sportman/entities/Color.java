package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Colors")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Color implements Serializable {
    private static final long serialVersionUID = 4473725630148209415L;
    @Id
    @Size(max = 6)
    @Column(name = "color_hex", nullable = false, length = 6)
    String colorHex;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    Boolean isDeleted;

}