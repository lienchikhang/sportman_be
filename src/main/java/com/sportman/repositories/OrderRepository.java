package com.sportman.repositories;

import com.sportman.entities.Order;
import com.sportman.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    public Page<Order> findAll(Pageable pageable);

    public Page<Order> findAll(Specification<Order> spec, Pageable pageable);

    public Optional<Order> findByIdAndUser(String id, User user);
}
