package com.webbook.resourceserver.services;


import io.jsonwebtoken.JwtException;

public interface JwtService {

    public boolean getUserActivity(String token);
    public String getUserEmail(String token);
    public Boolean validateToken(String token) throws JwtException,IllegalArgumentException;

}
