package com.webbook.resourceserver.filters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbook.resourceserver.authentication.ResourceServerAuthentication;
import com.webbook.resourceserver.dto.resourceserverDTO.ResourceServerCredentialsDTO;
import com.webbook.resourceserver.dto.resourceserverDTO.ResourceServerRequestDTO;
import com.webbook.resourceserver.exceptions.authExceptions.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationServerConnectFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServerConnectFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try{
        var objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        var resourceServerRequestDTO = objectMapper
                .readValue(httpServletRequest.getInputStream(), ResourceServerRequestDTO.class);
        var authentication = new ResourceServerAuthentication(resourceServerRequestDTO.getResourceServerCredentialsDTO().getResourceServerId(),
                resourceServerRequestDTO.getResourceServerCredentialsDTO().getResourceServerId());
        Authentication resultAuthentication = authenticationManager.authenticate(authentication);
        if(resultAuthentication.isAuthenticated()){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }else {
            throw new BadCredentialsException("Resource server credentials doesn't correct!");
        }
        } catch (
            JsonMappingException e){
            SecurityContextHolder.clearContext();
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.sendError(e.getHttpStatus().value());
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/addEnrolledUsers") || !request.getServletPath().equals("/addEnrolledUser");
    }
}
