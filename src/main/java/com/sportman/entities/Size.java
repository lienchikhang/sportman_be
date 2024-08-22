package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "Sizes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Size implements Serializable {
    @Id
    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "size_tag", nullable = false, length = 10, unique = true)
    String sizeTag;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    Boolean isDeleted;

}