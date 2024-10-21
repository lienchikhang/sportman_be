package com.sportman.repositories;

import com.sportman.entities.Product;
import com.sportman.entities.Rate;
import com.sportman.entities.RateId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, RateId> {

    public Page<Rate> findAll(Pageable pageable);

    public Page<Rate> findAllByProduct(Pageable pageable, Product product);

    public long countAllByProduct(Product product);
}
