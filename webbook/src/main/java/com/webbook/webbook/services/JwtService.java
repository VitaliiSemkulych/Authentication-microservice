package com.webbook.webbook.services;

import com.webbook.webbook.dto.AuthenticationResponseDTO;
import com.webbook.webbook.enums.RefreshTokenState;
import com.webbook.webbook.enums.TokenType;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;

public interface JwtService {
    public Date getTokenExpirationDate(String token, TokenType tokenType);
    public void cleanRefreshTokenByAccessToken(String accessToken,String deviceInfo);
    public void cleanRefreshTokenByUserEmail(String userEmail, String deviceInfo);
    public boolean isAlreadyLogged(String userEmail,String deviceInfo);
    public String getUserEmail(String token, TokenType tokenType);
    public Boolean validateRefreshToken(String token);
    public Boolean isTokenExpired(String token, TokenType tokenType) throws JwtException,IllegalArgumentException;
    public AuthenticationResponseDTO  updateTokenResponse(String userName,
                                                         Collection<? extends GrantedAuthority> grantedAuthorities,
                                                         String deviceInfo,
                                                         RefreshTokenState refreshTokenState,boolean active);
    public AuthenticationResponseDTO createTokenResponse(String userEmail,
                                                            Collection<? extends GrantedAuthority> grantedAuthorities,
                                                            String deviceInfo, RefreshTokenState refreshTokenState,boolean active,boolean rememberMe);

}
