package com.webbook.webbook.services;

import com.webbook.webbook.dto.UserInfoDTO;
import com.webbook.webbook.exceptions.NoSuchUserException;

import com.webbook.webbook.models.User;
import com.webbook.webbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;


    @Override
    public UserInfoDTO getUserInfoByEmail(String email) throws NoSuchUserException {
        User user=userRepository.findByEmail(email).orElseThrow(NoSuchUserException::new);
        return UserInfoDTO.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .phoneNumber( user.getPhoneNumber())
                .build();
    }

    @Override
    public User getUserByEmail(String email) throws NoSuchUserException {
        return userRepository.findByEmail(email).orElseThrow(NoSuchUserException::new);
    }
}
