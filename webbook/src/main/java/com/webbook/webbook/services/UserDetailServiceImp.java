package com.webbook.webbook.services;

import com.webbook.webbook.models.securityUserDetailsModel.UserDetailImp;
import com.webbook.webbook.repository.UserRepository;
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
                .map(user-> new UserDetailImp(user))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //           User userDetail= new User(
//                user.getEmail(),user.getPassword(), user.getRoles().stream()
//                .map(role -> role.getAuthorities())
//                .flatMap(authorities -> authorities.stream())
//                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
//                        .collect(Collectors.toList())
//        );
//
//        return userDetail;

//        Optional<com.webbook.webbook.models.User> us =userRepository.findByEmail(userName);
//        if(us.isPresent()){
//            com.webbook.webbook.models.User user = us.get();
//            User u= new User(user.getEmail(),user.getPassword(), List.of(new SimpleGrantedAuthority(":nothing")));
//            System.out.println("Logined user "+u);
//            return u;
//        }else{
//            System.out.println("User Not found");
//            new UsernameNotFoundException("User not found");
//        }
//
//        System.out.println("User fuctionality: we cant be here "+us);
//        return null;
    }


}
