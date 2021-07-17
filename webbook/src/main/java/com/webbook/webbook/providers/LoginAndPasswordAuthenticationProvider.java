package com.webbook.webbook.providers;

import com.webbook.webbook.exceptions.authExceptions.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginAndPasswordAuthenticationProvider implements AuthenticationProvider {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public LoginAndPasswordAuthenticationProvider(@Qualifier("userDetailServiceImp") UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
                                                  PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());
            if (passwordEncoder.matches(String.valueOf(authentication.getCredentials()), user.getPassword())) {
                if(user.isEnabled()) {
                    return new UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
                }else{
                    throw new JwtAuthenticationException("Account blocked.");
                }
                }else {
                throw new BadCredentialsException("Password is not correct");

            }
        }catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new JwtAuthenticationException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }



    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass);
    }
}
