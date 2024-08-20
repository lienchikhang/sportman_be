package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "Sizes")
public class Size implements Serializable {
    private static final long serialVersionUID = -3048298479601854048L;
    @Id
    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "size_tag", nullable = false, length = 10)
    private String sizeTag;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

}