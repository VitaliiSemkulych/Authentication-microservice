package com.webbook.webbook.controllers;

import com.webbook.webbook.models.Role;
import com.webbook.webbook.services.RoleService;
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
public class RoleController {

    private final RoleService roleService;


    @GetMapping("/getRolesList")
    public ResponseEntity<?> getRolesList(@RequestBody Pageable pageable){
        return ResponseEntity.ok(roleService.getRolesPage(pageable));
    }

    @PostMapping("/addRole")
    public ResponseEntity<?> addRole(@RequestBody Optional<Role> optionalRole){

        if(optionalRole.isPresent()){
            return ResponseEntity.ok(roleService.save(optionalRole.get()));
        }else {
            return ResponseEntity.badRequest()
                    .body("Empty role.");
        }
    }


    @DeleteMapping("/removeRole/{id}")
    public ResponseEntity<?> removeAuthority(@PathVariable Long id){
        try{
            return ResponseEntity.ok(roleService.remove(id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateRole")
    public ResponseEntity<?> updateRole(@RequestBody Optional<Role> optionalRole){

        if (optionalRole.isPresent()){
            try {
                return ResponseEntity.ok(roleService.update(optionalRole.get()));
            }catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }else {
            return ResponseEntity.badRequest()
                    .body("Empty role.");
        }
    }


}
