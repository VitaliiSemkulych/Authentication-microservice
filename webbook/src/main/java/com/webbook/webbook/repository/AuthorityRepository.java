package com.webbook.webbook.repository;

import com.webbook.webbook.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long> {

    Optional<Authority> findByAuthorityName(String authorityName);
}
