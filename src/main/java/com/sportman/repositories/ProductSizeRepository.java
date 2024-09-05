package com.sportman.repositories;

import com.sportman.entities.Product;
import com.sportman.entities.ProductSize;
import com.sportman.entities.ProductSizeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, ProductSizeId> {

    public List<ProductSize> findAllByProduct(Product product);

}
