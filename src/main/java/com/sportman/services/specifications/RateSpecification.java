package com.sportman.services.specifications;

import com.sportman.entities.Product;
import com.sportman.entities.Rate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class RateSpecification {

    public static Specification<Rate> hasRate(int rate) {
        return (root, query, criteriaBuilder) -> {
            if (rate == 0) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("rateStar"), rate);
        };
    }

    public static Specification<Rate> hasProduct(Product product) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(product)) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("product"), product);
        };
    }

}
