package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class AbstractEntity {

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    Boolean isDeleted;

    @NotNull
    @Column(name = "created_at")
    @CreationTimestamp
    LocalDate createdAt;

}
