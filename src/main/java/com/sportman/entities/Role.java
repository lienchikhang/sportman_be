package com.sportman.entities;

import com.sportman.enums.EnumRole;
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
@Table(name = "Roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role implements Serializable {
    @Id
    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    EnumRole name;

    @Column(name = "role_desc", nullable = false)
    String roleDesc;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    Set<RolePermission> rolePermissions;

}