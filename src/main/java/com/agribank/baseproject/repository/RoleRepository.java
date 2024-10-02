package com.agribank.baseproject.repository;

import com.agribank.baseproject.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "SELECT * FROM roles WHERE name = :name ", nativeQuery = true)
    Role findRoleByName(@Param("name") String roleName);

    @Query(value = "SELECT * FROM roles WHERE id IN :roleIds", nativeQuery = true)
    List<Role> findRolesInIdList(@Param("roleIds") List<Integer> roleIds);
}
