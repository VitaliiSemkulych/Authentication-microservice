package com.webbook.webbook.services;

import com.webbook.webbook.models.Authority;
import com.webbook.webbook.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityServiceImp implements AuthorityService{

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImp(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public Page<Authority> getAuthoritiesPage(Pageable pageable) {
        return authorityRepository.findAll(pageable);
    }

    @Override
    public Authority remove(Long authorityId) {
        Optional<Authority> authorityOptional = authorityRepository.findById(authorityId);
       if(authorityOptional.isPresent()) {
           authorityRepository.delete(authorityOptional.get());
           return authorityOptional.get();
       }else{
        throw new IllegalArgumentException(String.format("Authority with Id: %s  isn't exist.",authorityId));
    }

    }

    @Override
    public Authority update(Authority authority) {
        if(authorityRepository.findById(authority.getId()).isPresent()){
           return authorityRepository.save(authority);
        }else{
            throw new IllegalArgumentException(String.format("Authority with Id: %s  isn't exist",
                    authority.getId()));

        }
    }
}
