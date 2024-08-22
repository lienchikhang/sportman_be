package com.sportman.entities;

import com.sportman.enums.EnumRole;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class RolePermissionId {
    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumRole roleName;

    @Column(name = "per_name", nullable = false)
    private String perName;

}