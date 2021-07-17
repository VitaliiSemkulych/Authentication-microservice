package com.webbook.webbook.repository;

import com.webbook.webbook.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
