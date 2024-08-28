package com.sportman.repositories;

import com.sportman.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    public boolean existsByProductName(String productName);

}
