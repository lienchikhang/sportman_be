package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BlackListTokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlackListToken {

    @Id
    @Column(name = "refresh_id", nullable = false, unique = true)
    String refreshId;

    @Column(name = "exp", nullable = false)
    Date expirationTime;

}
