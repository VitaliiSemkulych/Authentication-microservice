package com.webbook.resourceserver.services;

import com.webbook.resourceserver.model.User;

import java.util.List;

public interface EnrollUserService {
    public void addEnrolledUsers(List<User> users);
    public void addEnrolledUser(User user);

}
