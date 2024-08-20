package com.sportman.repositories;

import com.sportman.entities.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends CrudRepository<Club, String> {

    public Optional<Club> findClubByClubNameAndIsDeletedFalse(String clubName);

    public Page<Club> findAllByIsDeletedFalse(Pageable pageable);

}
