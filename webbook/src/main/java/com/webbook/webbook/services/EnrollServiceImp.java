package com.webbook.webbook.services;


import com.webbook.webbook.dto.UserRegisterDTO;
import com.webbook.webbook.enums.LoginType;
import com.webbook.webbook.exceptions.enrollExeption.BadActivationCodeException;
import com.webbook.webbook.models.Role;
import com.webbook.webbook.models.User;
import com.webbook.webbook.repository.RoleRepository;
import com.webbook.webbook.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class EnrollServiceImp implements EnrollService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final RoleService roleService;


    @Override
    public void enroll(UserRegisterDTO enrollUser) {

        User user = User.builder().userName(enrollUser.getUserName())
                .email(enrollUser.getUserEmail())
                .password(passwordEncoder.encode(enrollUser.getPassword()))
                .roles(List.of(roleService.getUserRole()))
                .active(false)
                .loginType(Stream.of(LoginType.LOCAL).collect(Collectors.toList()))
                .activationCode(UUID.randomUUID().toString())
                .build();
        userRepository.save(user);
        try {
            mailSenderService.sendMime(user);
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.println("Implement Exception!!!");
            e.printStackTrace();
        }

    }


    public void activateUser(String code) throws BadActivationCodeException {

        var user=userRepository.findByActivationCode(code)
                .orElseThrow(()->new BadActivationCodeException(code));
        user.setActive(true);
        user.setActivationCode(null);
        userRepository.save(user);

    }


    @Override
    public boolean isUserExist(String email) {
        return userRepository.existsByEmail(email);
    }


}
