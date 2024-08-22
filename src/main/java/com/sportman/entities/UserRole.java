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
@Table(name = "user_role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole implements Serializable {
    @EmbeddedId
    UserRoleId id;

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_name", nullable = false)
    Role roleName;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

}