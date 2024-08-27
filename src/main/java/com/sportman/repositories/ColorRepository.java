package com.sportman.repositories;

import com.sportman.entities.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends CrudRepository<Color, String> {

    Page<Color> findAllByIsDeletedFalse(Pageable pageable);

}
