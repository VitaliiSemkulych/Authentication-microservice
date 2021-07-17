package com.webbook.resourceserver.services;


import com.webbook.resourceserver.model.securityUserDetailsModel.UserDetailImp;
import com.webbook.resourceserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userDetailServiceImp")
public class UserDetailServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) {
        return userRepository
                .findByEmail(userEmail)
                .map(user-> {
                    user.setActive(true);
                    return new UserDetailImp(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}
