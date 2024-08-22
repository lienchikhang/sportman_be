package com.sportman.repositories;

import com.sportman.entities.RolePermission;
import com.sportman.entities.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
}
