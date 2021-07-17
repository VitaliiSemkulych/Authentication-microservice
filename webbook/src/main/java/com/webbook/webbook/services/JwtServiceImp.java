package com.webbook.webbook.services;

import com.webbook.webbook.dto.AuthenticationResponseDTO;
import com.webbook.webbook.enums.RefreshTokenState;
import com.webbook.webbook.enums.TokenType;
import com.webbook.webbook.models.Device;
import com.webbook.webbook.repository.DeviceRepository;
import com.webbook.webbook.repository.UserRepository;
import com.webbook.webbook.utils.ConfigurationParameterUtility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtServiceImp implements JwtService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final ConfigurationParameterUtility configurationParameterUtility;

    public boolean isAlreadyLogged(String userEmail,String deviceInfo){
        return deviceRepository
                .existsByUserAndDeviceInfo(userRepository
                        .findByEmail(userEmail).get(),deviceInfo);

    }


    @Override
    public void cleanRefreshTokenByAccessToken(String accessToken,String deviceInfo) {
        cleanRefreshTokenByUserEmail(getUserEmail(accessToken,TokenType.ACCESS_TOKEN),deviceInfo);
    }

    @Override
    public void cleanRefreshTokenByUserEmail(String userEmail, String deviceInfo) {
        this.deviceRepository.deleteDevice(deviceInfo,this.userRepository.findByEmail(userEmail).get());

    }


    @Override
    public String getUserEmail(String token, TokenType tokenType) {
        return Jwts.parser().setSigningKey(configurationParameterUtility.getPublicTokenSecret(tokenType)).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public Boolean validateRefreshToken(String token){
        return isTokenExpired(token,TokenType.REFRESH_TOKEN) &&
                deviceRepository.findByRefreshToken(token).isPresent();
    }

    @Override
    public Boolean isTokenExpired(String token, TokenType tokenType) throws JwtException, IllegalArgumentException {
        return !getTokenExpirationDate(token, tokenType).before(new Date());
    }


    @Override
    public AuthenticationResponseDTO  updateTokenResponse(String userName,
                       Collection<? extends GrantedAuthority> grantedAuthorities,
                       String deviceInfo,
                       RefreshTokenState refreshTokenState,boolean active){
        var rememberMe=deviceRepository.findRememberMeStateByRefreshToken(deviceInfo,
                userRepository.findByEmail(userName).get());
        return createTokenResponse(userName,grantedAuthorities,deviceInfo,refreshTokenState,active,rememberMe);
    }
    @Override
    public AuthenticationResponseDTO createTokenResponse(String userName,
                                                            Collection<? extends GrantedAuthority> grantedAuthorities,
                                                            String deviceInfo,
                                                            RefreshTokenState refreshTokenState,boolean active,
                                                         boolean rememberMe){
        var tokenAccess = createToken(userName, grantedAuthorities, TokenType.ACCESS_TOKEN,active,rememberMe);
        var tokenRefresh= createToken(userName, grantedAuthorities, TokenType.REFRESH_TOKEN,active,rememberMe);
        if (refreshTokenState.equals(RefreshTokenState.CREATE))
            saveRefresh(userName, tokenRefresh, deviceInfo, rememberMe);
        else
            updateRefresh(userName, tokenRefresh, deviceInfo);

        return  AuthenticationResponseDTO.builder().accessToken(tokenAccess)
                .refreshToken(tokenRefresh).tokenType(configurationParameterUtility.getTokenTypeValue()).build();
    }


    @Override
    public Date getTokenExpirationDate(String token, TokenType tokenType){
        return Jwts.parser().setSigningKey(configurationParameterUtility.getPublicTokenSecret(tokenType))
                .parseClaimsJws(token)
                .getBody().getExpiration();
    }



    private String createToken(String userName,
                               Collection<? extends GrantedAuthority> grantedAuthorities, TokenType tokenType,boolean active,boolean rememberMe) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("authorities", grantedAuthorities);
        claims.put("active",active);
        Date now = new Date();
        Date validity = new Date(now.getTime() + configurationParameterUtility.getTokenExpirationTime(tokenType,rememberMe));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, configurationParameterUtility.getPrivateTokenSecret(tokenType))
                .compact();
    }



    private void saveRefresh(String userEmail,String refreshToken, String deviceInfo,boolean rememberMe) {
         deviceRepository.save(Device.builder()
                 .refreshToken(refreshToken)
                 .expirationDate(getTokenExpirationDate(refreshToken,TokenType.REFRESH_TOKEN))
                 .deviceInfo(deviceInfo)
                 .rememberMe(rememberMe)
                 .user(userRepository.findByEmail(userEmail).get())
                 .build());
    }

        private void updateRefresh(String userEmail,String refreshToken, String deviceInfo) {
            var expirationDate =getTokenExpirationDate(refreshToken,TokenType.REFRESH_TOKEN);
             deviceRepository.updateRefreshToken(refreshToken,deviceInfo,userRepository.findByEmail(userEmail).get(),expirationDate);

        }

}
