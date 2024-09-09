package com.sportman.repositories;

import com.sportman.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, CartDetailId> {

    public Page<CartDetail> findAllByCart(Cart cart, Pageable pageable);

    public CartDetail findByCartAndProductAndSize(Cart cart, Product product, Size size);
}
