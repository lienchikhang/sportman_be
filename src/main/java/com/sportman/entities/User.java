package com.sportman.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Users")
public class User extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "username", nullable = false, unique = true)
    String username;

    @NotNull
    @Column(name = "first_name", nullable = false)
    String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    String lastName;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    String password;


    @Column(name = "dob", nullable = true)
    LocalDate dob;

    @Lob
    @Column(name = "avatar")
    String avatar;

    @ColumnDefault("0")
    @Column(name = "balance")
    Integer balance;

    @Size(max = 255)
    @Column(name = "app_third_party_id")
    String appThirdPartyId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    Set<UserRole> userRole = new HashSet<>();

}