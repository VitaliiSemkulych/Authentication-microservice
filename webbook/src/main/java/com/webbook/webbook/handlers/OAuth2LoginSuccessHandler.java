package com.webbook.webbook.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbook.webbook.dto.AuthenticationResponseDTO;
import com.webbook.webbook.enums.LoginType;
import com.webbook.webbook.enums.RefreshTokenState;
import com.webbook.webbook.models.User;
import com.webbook.webbook.repository.UserRepository;
import com.webbook.webbook.services.JwtService;
import com.webbook.webbook.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${jwt.header.UserAgent}")
    private String userAgentHeader;


    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final RoleService roleService;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {


        AuthenticationResponseDTO authenticationResponseDTO ;
        var deviceInfo = httpServletRequest.getHeader(userAgentHeader);
        LoginType loginType = LoginType.getLoginTypeByClientName(((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId());
        User user = getUser((OAuth2User) authentication.getPrincipal(),loginType);

        if(userRepository.existsByEmail(user.getEmail())) {
            if (!user.getLoginType().contains(loginType)){
                user.getLoginType().add(loginType);
                user.setUserName(((OAuth2User) authentication.getPrincipal())
                        .<String>getAttribute("name"));
            }
            authenticationResponseDTO=user.getDevices().stream()
                    .anyMatch(device -> device.getDeviceInfo().equals(deviceInfo))?
                    updateTokenResponse(user,deviceInfo,authentication.isAuthenticated()):
                    createTokenResponse(user,deviceInfo,authentication.isAuthenticated());
        } else {
            userRepository.save(user);
            authenticationResponseDTO = createTokenResponse(user, deviceInfo, authentication.isAuthenticated());
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(httpServletResponse.getWriter(), authenticationResponseDTO);
        httpServletResponse.getWriter().flush();


    }

    private User getUser(OAuth2User oAuth2User,LoginType loginType){
        return userRepository.findByEmail(oAuth2User.<String>getAttribute("email")).orElseGet(() -> User.builder()
                .userName(oAuth2User.<String>getAttribute("name"))
                .email(oAuth2User.<String>getAttribute("email"))
                .roles(Collections.singletonList(roleService.getUserRole()))
                .loginType(Collections.singletonList(loginType))
                .build());
    }

    private AuthenticationResponseDTO updateTokenResponse(User user,String deviceInfo, boolean isAuthenticated){
        return jwtService.updateTokenResponse(
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getAuthorities())
                        .flatMap(authorities -> authorities.stream())
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                        .collect(Collectors.toList()),
                deviceInfo,
                RefreshTokenState.UPDATE,
                isAuthenticated);
    }

    private AuthenticationResponseDTO createTokenResponse(User user,String deviceInfo, boolean isAuthenticated){
        return jwtService.createTokenResponse(
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getAuthorities())
                        .flatMap(authorities -> authorities.stream())
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                        .collect(Collectors.toList()),
                deviceInfo,
                RefreshTokenState.CREATE,
                isAuthenticated,
                false);
    }
}
