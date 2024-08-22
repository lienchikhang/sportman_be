package com.sportman.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission implements Serializable {
    @Id
    @Column(name = "name", nullable = false, unique = true)
    String name;

    @NotNull
    @Column(name = "per_desc", nullable = false)
    String perDesc;
}