package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JavaType;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Clubs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Club implements Serializable {
    @Id
    @Size(max = 255)
    @Column(name = "club_name", nullable = false, unique = true)
    String clubName;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    Boolean isDeleted = false;

    @Size(max = 7)
    @NotNull
    @Column(name = "color_hex", nullable = false, length = 7)
    String colorHex;

    @Size(max = 3)
    @NotNull
    @Column(name = "short_name", nullable = false, length = 3)
    String shortName;

}