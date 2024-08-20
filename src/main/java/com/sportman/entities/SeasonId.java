package com.sportman.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
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
public class SeasonId implements Serializable {
    private static final long serialVersionUID = 7124201625903159249L;
    @NotNull
    @Column(name = "year_start", nullable = false)
    private Integer yearStart;

    @NotNull
    @Column(name = "year_end", nullable = false)
    private Integer yearEnd;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SeasonId entity = (SeasonId) o;
        return Objects.equals(this.yearEnd, entity.yearEnd) &&
                Objects.equals(this.yearStart, entity.yearStart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(yearEnd, yearStart);
    }

}