package com.sportman.repositories;

import com.sportman.entities.Season;
import com.sportman.entities.SeasonId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends CrudRepository<Season, SeasonId> {

    public Page<Season> findAllByIsDeletedFalse(Pageable pageable);

    public Season findByIdAndAndIsDeletedFalse(SeasonId seasonId);

}
