package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
public class RolePermissionId implements Serializable {
    private static final long serialVersionUID = 1004555359179871902L;
    @Size(max = 255)
    @NotNull
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Size(max = 255)
    @NotNull
    @Column(name = "per_name", nullable = false)
    private String perName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RolePermissionId entity = (RolePermissionId) o;
        return Objects.equals(this.perName, entity.perName) &&
                Objects.equals(this.roleName, entity.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(perName, roleName);
    }

}