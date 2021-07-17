package com.webbook.webbook.controllers;

import com.webbook.webbook.dto.UserInfoDTO;
import com.webbook.webbook.exceptions.NoSuchUserException;
import com.webbook.webbook.models.User;
import com.webbook.webbook.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/manageUsers")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @RequestMapping("/getUserInfo")
    public UserInfoDTO getUser(Principal principal) throws NoSuchUserException {
        return userService.getUserInfoByEmail(principal.getName());
    }


}
