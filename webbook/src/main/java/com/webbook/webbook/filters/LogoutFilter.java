package com.webbook.webbook.filters;

import com.webbook.webbook.enums.TokenType;
import com.webbook.webbook.exceptions.authExceptions.JwtAuthenticationException;
import com.webbook.webbook.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutFilter extends OncePerRequestFilter {

    @Value("${jwt.access.header}")
    private String authorizationHeader;
    @Value("${jwt.header.UserAgent}")
    private String userAgentHeader;
    private JwtService jwtService;

    @Autowired
    public void setJwtBuildTokenService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {


            var accessToken = httpServletRequest.getHeader(authorizationHeader);
            var deviceInfo = httpServletRequest.getHeader(userAgentHeader);
            if (accessToken != null &&
                    jwtService.isTokenExpired(accessToken, TokenType.ACCESS_TOKEN) &&
                    deviceInfo!=null) {
                jwtService.cleanRefreshTokenByAccessToken(accessToken,deviceInfo);
                SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
                securityContextLogoutHandler.logout(httpServletRequest, httpServletResponse, null);
                SecurityContextHolder.clearContext();
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            } else {
                SecurityContextHolder.clearContext();
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/perform_logout") || !request.getMethod().equals(HttpMethod.GET.toString());

    }
}
