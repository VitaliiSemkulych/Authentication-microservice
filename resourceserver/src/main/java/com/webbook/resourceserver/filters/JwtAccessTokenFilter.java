package com.webbook.resourceserver.filters;


import com.webbook.resourceserver.authentication.AccessTokenAuthentication;
import com.webbook.resourceserver.exceptions.authExceptions.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
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

public class JwtAccessTokenFilter extends OncePerRequestFilter {


    @Value("${jwt.access.header}")
    private String authorizationHeader;

    private final AuthenticationManager authenticationManager;


    @Autowired
    public JwtAccessTokenFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        var accessToken = httpServletRequest.getHeader(authorizationHeader);

        try {
            if(accessToken!=null){
            var authentication = new AccessTokenAuthentication(accessToken);
            var result = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(result);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            }else{
                throw new JwtAuthenticationException("No authorization token", HttpStatus.FORBIDDEN);
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.sendError(e.getHttpStatus().value());
        }


    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return  request.getServletPath().equals("/addEnrolledUsers") ||
                request.getServletPath().equals("/addEnrolledUser");
    }


}

