package com.webbook.resourceserver.authentication;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;


public class AccessTokenAuthentication implements Authentication {

    private final Optional<String> accessToken;

    private final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    public AccessTokenAuthentication(String token) {
        this.accessToken=Optional.of(token);
        this.usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(null,null);
    }

    public AccessTokenAuthentication(Object principal, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken=Optional.empty();
        this.usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(principal,"",authorities);

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usernamePasswordAuthenticationToken.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return this.usernamePasswordAuthenticationToken.getCredentials();
    }

    @Override
    public Object getDetails() {
        return this.usernamePasswordAuthenticationToken.getDetails();
    }


    @Override
    public Object getPrincipal() {
        return this.accessToken.isPresent()? (Principal)() -> this.accessToken.get()
                :this.usernamePasswordAuthenticationToken.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return this.usernamePasswordAuthenticationToken.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        Assert.isTrue(!authenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }

    @Override
    public String getName() {
        return accessToken.isPresent()? this.accessToken.get()
                :this.usernamePasswordAuthenticationToken.getName();

    }
}
