package com.webbook.resourceserver.services;

import com.webbook.resourceserver.model.User;
import com.webbook.resourceserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EnrollUserServiceImp implements EnrollUserService{
    private final UserRepository userRepository;

    @Autowired
    public EnrollUserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void addEnrolledUsers(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    public void addEnrolledUser(User user) {
        userRepository.save(user);
    }
}
