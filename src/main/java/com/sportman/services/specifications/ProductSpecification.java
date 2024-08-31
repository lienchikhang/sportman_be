package com.sportman.services.specifications;

import com.sportman.entities.Product;
import com.sportman.entities.ProductSize;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ProductSpecification {

    public static Specification<Product> hasDeleted(Boolean isDeleted) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            if (Objects.isNull(isDeleted)) return criteriaBuilder.conjunction();

            criteriaQuery.multiselect(
                    root.get("id"),
                    root.get("productName"),
                    root.get("productPrice"),
                    root.get("frontImage"),
                    root.get("backImage"),
                    root.get("colors"));

            return criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
        };
    }

    public static Specification<Product> hasName(String productName) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            if (Objects.isNull(productName)) return criteriaBuilder.conjunction();

            criteriaQuery.multiselect(
                    root.get("id"),
                    root.get("productName"),
                    root.get("productPrice"),
                    root.get("frontImage"),
                    root.get("backImage"),
                    root.get("colors"));

            return criteriaBuilder.like(root.get("productName"), "%" + productName + "%");
        };
    }

    public static Specification<Product> hasClub(String clubName) {

        return (root, criteriaQuery, criteriaBuilder) -> {

            if (Objects.isNull(clubName)) return criteriaBuilder.conjunction();

            criteriaQuery.multiselect(
                    root.get("id"),
                    root.get("productName"),
                    root.get("productPrice"),
                    root.get("frontImage"),
                    root.get("backImage"),
                    root.get("colors"));

            return criteriaBuilder.equal(
                    root.get("club").get("clubName"),
                    clubName.trim().toUpperCase()
            );
        };

    }

    public static Specification<Product> hasPrice(Integer price) {

        return (root, criteriaQuery, criteriaBuilder) -> {

            log.info("beforeee");
            log.info(Objects.isNull(price) ? "true" : "false");

            if (Objects.isNull(price)) return criteriaBuilder.conjunction();

            criteriaQuery.multiselect(
                    root.get("id"),
                    root.get("productName"),
                    root.get("productPrice"),
                    root.get("frontImage"),
                    root.get("backImage"),
                    root.get("colors"));

            log.info("run in has price");
            log.info(price.toString());

            return criteriaBuilder.lessThanOrEqualTo(root.get("productPrice"), price);
        };

    }

    public static Specification<Product> hasSeason(Integer yearStart, Integer yearEnd) {

        return (root, criteriaQuery, criteriaBuilder) -> {

            if (Objects.isNull(yearStart) && Objects.isNull(yearEnd)) return criteriaBuilder.conjunction();

            criteriaQuery.multiselect(
                    root.get("id"),
                    root.get("productName"),
                    root.get("productPrice"),
                    root.get("frontImage"),
                    root.get("backImage"),
                    root.get("colors"));

            Predicate predicate = criteriaBuilder.conjunction();

            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("season").get("id").get("yearStart"), yearStart));
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("season").get("id").get("yearEnd"), yearEnd));

            return predicate;
        };

    }

    public static Specification<Product> hasSize(List<String> sizeTag) {
        return ((root, criteriaQuery, criteriaBuilder) -> {

            if (Objects.isNull(sizeTag)) return criteriaBuilder.conjunction();

            criteriaQuery.multiselect(
                    root.get("id"),
                    root.get("productName"),
                    root.get("productPrice"),
                    root.get("frontImage"),
                    root.get("backImage"),
                    root.get("colors"));

            Join<Product, ProductSize> stockJoin = root.join("stocks", JoinType.INNER);

            Predicate[] sizePredicates = sizeTag.stream()
                    .map(size -> criteriaBuilder.equal(stockJoin.get("sizeTag").get("sizeTag"), size)).toArray(Predicate[]::new);

            criteriaQuery.distinct(true);

            return criteriaBuilder.or(sizePredicates);
        });
    }

    public static Specification<Product> hasSort(String sort) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            if (Objects.isNull(sort)) return criteriaBuilder.conjunction();

            criteriaQuery.multiselect(
                    root.get("id"),
                    root.get("productName"),
                    root.get("productPrice"),
                    root.get("frontImage"),
                    root.get("backImage"),
                    root.get("colors"));

            Order orderByName;
            if (sort.trim().equals("asc")) {
                orderByName = criteriaBuilder.asc(root.get("createdAt"));
            } else {
                orderByName = criteriaBuilder.desc(root.get("createdAt"));
            }

            criteriaQuery.orderBy(orderByName);

            return criteriaBuilder.conjunction();

        };
    }

}
