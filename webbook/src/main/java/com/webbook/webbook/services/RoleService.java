package com.webbook.webbook.services;


import com.webbook.webbook.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    public Role save(Role role);
    public Page<Role> getRolesPage(Pageable pageable);
    public Role remove(Long roleId);
    public Role update(Role role);
    public Role getUserRole();
}
