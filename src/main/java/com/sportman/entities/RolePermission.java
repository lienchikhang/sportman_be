package com.sportman.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "role_permission")
public class RolePermission implements Serializable {
    private static final long serialVersionUID = -5005332616822472631L;
    @EmbeddedId
    private RolePermissionId id;

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_name", nullable = false)
    private Role roleName;

    @MapsId("perName")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "per_name", nullable = false)
    private Permission perName;

}