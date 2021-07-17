package com.webbook.resourceserver.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;

public class ResourceServerAuthentication implements Authentication {

    private final String resourceServerId;
    private final String resourceServerSecret;
    private final boolean authenticated;

    public ResourceServerAuthentication(String resourceServerId,String resourceServerSecret){
        this.resourceServerId=resourceServerId;
        this.resourceServerSecret=resourceServerSecret;
        this.authenticated=false;
    }

    public ResourceServerAuthentication(String resourceServerId,boolean authenticated){
        this.resourceServerId=resourceServerId;
        this.resourceServerSecret=null;
        this.authenticated=authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return resourceServerSecret;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return resourceServerId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        Assert.isTrue(!authenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }

    @Override
    public String getName() {
        return resourceServerId;
    }
}
