package com.webbook.resourceserver.services;


import com.webbook.resourceserver.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServiceImp implements JwtService {

    @Value("${jwt.publicAccessTokenSecret}")
    private String publicSecretAccessTokenKey;
    private final UserRepository userRepository;

    @Autowired
    public JwtServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(publicSecretAccessTokenKey).parseClaimsJws(token).getBody().getSubject();
    }


    @Override
    public boolean getUserActivity(String token) {
        return Jwts.parser().setSigningKey(publicSecretAccessTokenKey)
                .parseClaimsJws(token).getBody().get("active",Boolean.class);
    }

    @Override
    public Boolean validateToken(String token)  {
        return !getTokenExpirationDate(token).before(new Date());
    }



    private Boolean isTokenExpired(String token){
        return !getTokenExpirationDate(token).before(new Date());
    }


    private Date getTokenExpirationDate(String token){
        return Jwts.parser().setSigningKey(publicSecretAccessTokenKey)
                .parseClaimsJws(token)
                .getBody().getExpiration();
    }





}
