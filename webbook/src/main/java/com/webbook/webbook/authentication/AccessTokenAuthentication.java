package com.webbook.webbook.authentication;


import com.webbook.webbook.enums.TokenType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Optional;


public class AccessTokenAuthentication implements Authentication {

    private final Optional<String> accessToken;
    private final Optional<TokenType> tokenType;
    private final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    public AccessTokenAuthentication(String token,TokenType tokenType) {
        this.tokenType=Optional.of(tokenType);
        this.accessToken=Optional.of(token);
        this.usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(null,null);
    }

    public AccessTokenAuthentication(Object principal, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken=Optional.empty();
        this.tokenType=Optional.empty();
        this.usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(principal,"",authorities);

    }

    public TokenType getTokenType(){
        return this.tokenType.get();
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
        return this.accessToken.
                orElse(this.usernamePasswordAuthenticationToken.
                        getPrincipal().
                        toString());
//        return this.accessToken.isPresent()? (Principal)() -> this.accessToken.get()
//                :this.usernamePasswordAuthenticationToken.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return this.usernamePasswordAuthenticationToken.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        usernamePasswordAuthenticationToken.setAuthenticated(authenticated);
    }

    @Override
    public String getName() {
        return accessToken.orElse(this.usernamePasswordAuthenticationToken.getName());
//        return accessToken.isPresent()? this.accessToken.get()
//                :this.usernamePasswordAuthenticationToken.getName();

    }
}
