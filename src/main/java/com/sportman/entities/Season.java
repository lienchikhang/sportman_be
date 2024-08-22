package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "Seasons")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Season implements Serializable {
    @EmbeddedId
    SeasonId id;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    Boolean isDeleted = false;

}