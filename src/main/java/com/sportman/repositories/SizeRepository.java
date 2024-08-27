package com.sportman.repositories;

import com.sportman.dto.response.page.SizePageResponse;
import com.sportman.entities.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends CrudRepository<Size, String> {

    public Page<Size> findAllByIsDeletedFalse(Pageable pageable);

}
