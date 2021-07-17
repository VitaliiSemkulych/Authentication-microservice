package com.webbook.resourceserver.providers;


import com.webbook.resourceserver.authentication.ResourceServerAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class ResourceServerAuthenticationProvider implements AuthenticationProvider {

    @Value("#{resourceServer.resourceServerId}")
    private String resourceServerId;

    @Value("#{resourceServer.resourceServerSecret}")
    private String resourceServerSecret;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(resourceServerId.equals(authentication.getName())
                && resourceServerSecret.equals(authentication.getCredentials())){
            return new ResourceServerAuthentication(authentication.getName(),true);
        }
        throw new BadCredentialsException("Resource server credentials doesn't correct!");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ResourceServerAuthentication.class.equals(aClass);
    }
}
