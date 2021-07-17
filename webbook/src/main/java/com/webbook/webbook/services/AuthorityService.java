package com.webbook.webbook.services;

import com.webbook.webbook.models.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface AuthorityService {
    public Authority save(Authority authority);
    public Page<Authority> getAuthoritiesPage(Pageable pageable);
    public Authority remove(Long authorityId);
    public Authority update(Authority authority);
}
