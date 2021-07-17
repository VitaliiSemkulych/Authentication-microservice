package com.webbook.webbook.services;

import com.webbook.webbook.dto.UserInfoDTO;
import com.webbook.webbook.exceptions.NoSuchUserException;
import com.webbook.webbook.models.User;


public interface UserService {
    UserInfoDTO getUserInfoByEmail(String email) throws NoSuchUserException;
    User getUserByEmail(String email) throws NoSuchUserException;
}
