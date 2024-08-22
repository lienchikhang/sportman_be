package com.sportman.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "role_permission")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission implements Serializable {
    @EmbeddedId
    RolePermissionId id;

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_name", nullable = false)
    Role role;

    @MapsId("perName")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "per_name", nullable = false)
    Permission permission;

}