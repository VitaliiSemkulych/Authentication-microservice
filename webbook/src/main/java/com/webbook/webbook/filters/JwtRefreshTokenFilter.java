package com.webbook.webbook.filters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbook.webbook.authentication.AccessTokenAuthentication;
import com.webbook.webbook.dto.AuthenticationResponseDTO;
import com.webbook.webbook.enums.RefreshTokenState;
import com.webbook.webbook.enums.TokenType;
import com.webbook.webbook.exceptions.authExceptions.JwtAuthenticationException;
import com.webbook.webbook.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtRefreshTokenFilter extends OncePerRequestFilter {

//    @Value("${jwt.jwt.tokenType.value}")
//    private String tokenTypeValue;
//
    @Value("${jwt.refresh.header}")
    private String refreshHeader;
    @Value("${jwt.header.UserAgent}")
    private String userAgentHeader;

    private final AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private ObjectMapper objectMapper;


    @Autowired
    public JwtRefreshTokenFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setJwtBuildTokenService(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try {
            var refreshToken = httpServletRequest.getHeader(refreshHeader);
            if(refreshToken==null) {
                SecurityContextHolder.clearContext();
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
                Authentication authentication = authenticationManager.authenticate(
                        new AccessTokenAuthentication(refreshToken,
                        TokenType.REFRESH_TOKEN));
                   httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                   objectMapper.writeValue(httpServletResponse.getWriter(),
                           jwtService.updateTokenResponse(authentication.getName(),
                                   authentication.getAuthorities(),
                                   httpServletRequest.getHeader(userAgentHeader),
                                   RefreshTokenState.UPDATE,
                                   authentication.isAuthenticated()));
                httpServletResponse.getWriter().flush();

        } catch (JsonMappingException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/refresh") || !request.getMethod().equals(HttpMethod.GET.toString());

    }
}
