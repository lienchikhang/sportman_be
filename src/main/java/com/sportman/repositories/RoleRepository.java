package com.sportman.repositories;

import com.sportman.entities.Role;
import com.sportman.enums.EnumRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, EnumRole> {



    Page<Role> findAll(Pageable pageable);
}
