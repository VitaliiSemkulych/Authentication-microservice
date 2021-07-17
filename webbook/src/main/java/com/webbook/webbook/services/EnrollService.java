package com.webbook.webbook.services;

import com.webbook.webbook.dto.UserRegisterDTO;


public interface EnrollService {
    public void enroll(UserRegisterDTO enrollUser);
    public boolean isUserExist(String isEmailExist);
    public void activateUser(String code) throws IllegalArgumentException;
}
