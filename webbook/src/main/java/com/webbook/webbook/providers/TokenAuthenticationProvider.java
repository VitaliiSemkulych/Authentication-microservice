package com.webbook.webbook.providers;

import com.webbook.webbook.authentication.AccessTokenAuthentication;
import com.webbook.webbook.enums.TokenType;
import com.webbook.webbook.exceptions.authExceptions.JwtAuthenticationException;
import com.webbook.webbook.services.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {


    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public TokenAuthenticationProvider(@Qualifier("userDetailServiceImp") UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            var token = authentication.getName();
            var tokenType = ((AccessTokenAuthentication)authentication).getTokenType();

            if (tokenType.equals(TokenType.REFRESH_TOKEN)?
                    jwtService.validateRefreshToken(token):
                    jwtService.isTokenExpired(token, tokenType)) {
                var userDetails = this.userDetailsService.loadUserByUsername(jwtService.getUserEmail(token, tokenType));
                if(userDetails.isEnabled()) {
                    return new AccessTokenAuthentication(userDetails, userDetails.getAuthorities());
                }else{
                    throw new JwtAuthenticationException("Account blocked.");
                }
            }else{
                throw new JwtAuthenticationException("JWT token is expired or invalid");
            }

        }catch (JwtException | IllegalArgumentException | AuthenticationException e) {
            throw new JwtAuthenticationException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }


    @Override
    public boolean supports(Class<?> aClass) {
        return AccessTokenAuthentication.class.equals(aClass);
    }



}
