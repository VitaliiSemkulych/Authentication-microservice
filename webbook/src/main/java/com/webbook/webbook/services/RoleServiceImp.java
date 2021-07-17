package com.webbook.webbook.services;

import com.webbook.webbook.models.Authority;
import com.webbook.webbook.models.Role;
import com.webbook.webbook.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleServiceImp implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Page<Role> getRolesPage(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role remove(Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isPresent()) {
            roleRepository.delete(optionalRole.get());
            return optionalRole.get();
        }else{
            throw new IllegalArgumentException(String.format("Role with Id: %s  isn't exist.",roleId));
        }
    }

    @Override
    public Role update(Role role) {
        if(roleRepository.findById(role.getId()).isPresent()){
            return roleRepository.save(role);
        }else{
            throw new IllegalArgumentException(String.format("Role with Id: %s  isn't exist",
                    role.getId()));

        }
    }

    @Override
    public Role getUserRole() {
        Optional<Role> optionalRole= roleRepository.findByRoleName("ROLE_USER");
        return  optionalRole.orElseGet(()->new Role("ROLE_USER"));
    }
}
