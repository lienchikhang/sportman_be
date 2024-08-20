package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
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
public class Club implements Serializable {
    private static final long serialVersionUID = 1292640549325962761L;
    @Id
    @Size(max = 255)
    @Column(name = "club_name", nullable = false)
    private String clubName;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Size(max = 7)
    @NotNull
    @Column(name = "color_hex", nullable = false, length = 7)
    private String colorHex;

    @Size(max = 3)
    @NotNull
    @Column(name = "short_name", nullable = false, length = 3)
    private String shortName;

}