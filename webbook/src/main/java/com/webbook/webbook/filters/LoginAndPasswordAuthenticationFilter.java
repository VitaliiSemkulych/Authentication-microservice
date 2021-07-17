package com.webbook.webbook.filters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbook.webbook.dto.AuthenticationRequestDTO;
import com.webbook.webbook.dto.AuthenticationResponseDTO;
import com.webbook.webbook.enums.RefreshTokenState;
import com.webbook.webbook.exceptions.authExceptions.JwtAuthenticationException;
import com.webbook.webbook.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAndPasswordAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.header.UserAgent}")
    private String userAgentHeader;


    private final AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper;
    private JwtService jwtService;

    public LoginAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
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

            AuthenticationRequestDTO authenticationRequestDTO = objectMapper
                    .readValue(httpServletRequest.getInputStream(),
                            AuthenticationRequestDTO.class);

            var deviceInfo = httpServletRequest.getHeader(userAgentHeader);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDTO.getEmail(),
                            authenticationRequestDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

           if( jwtService.isAlreadyLogged(authentication.getName(),deviceInfo)) {
               jwtService.cleanRefreshTokenByUserEmail(authentication.getName(), deviceInfo);
           }
            AuthenticationResponseDTO authenticationResponseDTO = jwtService.createTokenResponse(
                    authentication.getName(),
                    authentication.getAuthorities(),
                    deviceInfo,
                    RefreshTokenState.CREATE,
                    authentication.isAuthenticated(),
                    authenticationRequestDTO.getRememberMe());


            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(httpServletResponse.getWriter(), authenticationResponseDTO);
            httpServletResponse.getWriter().flush();


        } catch (JsonMappingException e){
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

        return !request.getServletPath().equals("/login") ||
                !request.getMethod().equals(HttpMethod.POST.toString());
    }
}
