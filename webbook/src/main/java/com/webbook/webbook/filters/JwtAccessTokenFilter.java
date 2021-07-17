package com.webbook.webbook.filters;

import com.webbook.webbook.authentication.AccessTokenAuthentication;
import com.webbook.webbook.enums.TokenType;
import com.webbook.webbook.exceptions.authExceptions.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
public class JwtAccessTokenFilter extends OncePerRequestFilter {


    @Value("${jwt.access.header}")
    private String authorizationHeader;

    private final AuthenticationManager authenticationManager;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = httpServletRequest.getHeader(authorizationHeader);

            if(accessToken==null) {
                SecurityContextHolder.clearContext();
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(
                    new AccessTokenAuthentication(accessToken, TokenType.ACCESS_TOKEN)));
            filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login") ||
                request.getServletPath().equals("/refresh") ||
                request.getServletPath().equals("/logout") ||
                request.getServletPath().equals("/enroll") ||
                request.getServletPath().equals("/activate") ||
                request.getServletPath().equals("/test")
                || request.getServletPath().contains("/oauth")
                || request.getServletPath().equals("/favicon.ico");
    }


}

