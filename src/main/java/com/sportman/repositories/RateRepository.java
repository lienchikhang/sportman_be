package com.sportman.repositories;

import com.sportman.entities.Product;
import com.sportman.entities.Rate;
import com.sportman.entities.RateId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, RateId>, JpaSpecificationExecutor<Rate> {

    public Page<Rate> findAll(Specification<Rate> specs, Pageable pageable);

    public long count(Specification<Rate> specs);
}
