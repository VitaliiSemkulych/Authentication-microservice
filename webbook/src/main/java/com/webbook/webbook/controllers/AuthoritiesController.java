package com.webbook.webbook.controllers;

import com.webbook.webbook.models.Authority;
import com.webbook.webbook.services.AuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AuthoritiesController {

    private final AuthorityService authorityService;


    @GetMapping("/getAuthoritiesList")
    public ResponseEntity<?> getAuthoritiesList(@RequestBody Pageable pageable){
        return ResponseEntity.ok(authorityService.getAuthoritiesPage(pageable));
    }

    @PostMapping("/addAuthority")
    public ResponseEntity<?> addAuthority(@RequestBody Optional<Authority> authority){
        if(authority.isPresent()){
            return ResponseEntity.ok(authorityService.save(authority.get()));
        }else {
            return ResponseEntity.badRequest()
                .body("Empty authority.");
    }

    }

    @DeleteMapping("/removeAuthority/{id}")
    public ResponseEntity<?> removeAuthority(@PathVariable Long id){
        try {
            return ResponseEntity.ok(authorityService.remove(id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/updateAuthority")
    public ResponseEntity<?> updateAuthority(@RequestBody Optional<Authority> authorityOptional){
        if(authorityOptional.isPresent()){
            try{
                return ResponseEntity.ok(authorityService.update(authorityOptional.get()));
            }catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }else {
            return ResponseEntity.badRequest()
                .body("Empty authority.");
        }

    }

}
