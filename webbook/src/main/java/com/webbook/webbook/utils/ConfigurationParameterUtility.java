package com.webbook.webbook.utils;

import com.webbook.webbook.enums.TokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class ConfigurationParameterUtility {

    @Value("${jwt.privateAccessTokenSecret}")
    private String privateSecretAccessTokenKey;

    @Value("${jwt.publicAccessTokenSecret}")
    private String publicSecretAccessTokenKey;

    @Value("${jwt.privateRefreshTokenSecret}")
    private String privateSecretRefreshTokenKey;

    @Value("${jwt.publicRefreshTokenSecret}")
    private String publicSecretRefreshTokenKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpirationTime;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationTime;
    @Value("${jwt.refresh.rememberMe.expiration}")
    private long refreshTokenRememberMeExpirationTime;

    @Value("${jwt.tokenType.value}")
    private String tokenTypeValue;


    public long getTokenExpirationTime(TokenType tokenType,Boolean rememberMe){
        return tokenType.equals(TokenType.ACCESS_TOKEN)?accessTokenExpirationTime:
                rememberMe?refreshTokenRememberMeExpirationTime:refreshTokenExpirationTime;
    }

    public String getTokenTypeValue(){
        return tokenTypeValue;
    }

    public String getPrivateTokenSecret(TokenType tokenType){
        return tokenType.equals(TokenType.ACCESS_TOKEN)?
                privateSecretAccessTokenKey:privateSecretRefreshTokenKey;
    }
    public String getPublicTokenSecret(TokenType tokenType){
        return tokenType.equals(TokenType.ACCESS_TOKEN)?
                publicSecretAccessTokenKey:publicSecretRefreshTokenKey;
    }
}
