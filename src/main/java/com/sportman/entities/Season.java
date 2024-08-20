package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Seasons")
public class Season implements Serializable {
    private static final long serialVersionUID = -5878739877429404862L;
    @EmbeddedId
    private SeasonId id;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

}