package com.webbook.resourceserver.controllers;

import com.webbook.resourceserver.dto.resourceserverDTO.ResourceServerRequestDTO;
import com.webbook.resourceserver.dto.resourceserverDTO.UpdatingUserDTO;
import com.webbook.resourceserver.model.User;
import com.webbook.resourceserver.services.EnrollUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final EnrollUserService enrollUserService;


    @Autowired
    public UserController(EnrollUserService enrollUserService) {
        this.enrollUserService = enrollUserService;
    }

    @PostMapping("/addEnrolledUsers")
    public boolean addEnrolledUsers(@RequestBody Optional<ResourceServerRequestDTO<List<UpdatingUserDTO>>> updatingUsers){

        updatingUsers.orElseThrow(() -> new NullPointerException("Couldn't find users for updating."));

        enrollUserService.addEnrolledUsers(updatingUsers.get().getUserInfo().stream().map(updatingUserDTO ->
                User.builder()
                        .userName(updatingUserDTO.getUserName())
                        .email(updatingUserDTO.getEmail())
                        .build())
                .collect(Collectors.toList()));
        return true;
    }

    @PostMapping("/addEnrolledUser")
    public boolean addEnrolledUser(@RequestBody Optional<ResourceServerRequestDTO<UpdatingUserDTO>> updatingUser){

        updatingUser.orElseThrow(() -> new NullPointerException("Couldn't find user for updating."));

        enrollUserService.addEnrolledUser(User.builder()
                .userName(updatingUser.get().getUserInfo().getUserName())
                .email(updatingUser.get().getUserInfo().getEmail())
                .build());
        return true;
    }
}
