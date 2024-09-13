package com.sportman.services.specifications;

import com.sportman.entities.Order;
import com.sportman.entities.User;
import com.sportman.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class OrderSpecification {

    public static Specification<Order> hasStatus(OrderStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (Objects.isNull(status)) return criteriaBuilder.conjunction(); // and

            query.multiselect(
                    root.get("id"),
                    root.get("createdAt"),
                    root.get("status"),
                    root.get("orderDetails")
            );

            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Order> hasSort(String sort) {

        return (root, query, criteriaBuilder) -> {

            if (Objects.isNull(sort)) return criteriaBuilder.conjunction(); // and

            query.multiselect(
                    root.get("id"),
                    root.get("createdAt"),
                    root.get("status"),
                    root.get("orderDetails")
            );

            if (sort.trim().equals("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get("createdAt")));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            }

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Order> hasUser(User user) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(user)) return criteriaBuilder.conjunction();

            query.multiselect(
                    root.get("id"),
                    root.get("createdAt"),
                    root.get("status"),
                    root.get("orderDetails")
            );

            return criteriaBuilder.equal(root.get("user"), user);
        };
    }

}
