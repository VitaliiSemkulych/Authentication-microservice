package com.webbook.webbook.config.security;


import com.webbook.webbook.filters.JwtAccessTokenFilter;
import com.webbook.webbook.filters.JwtRefreshTokenFilter;
import com.webbook.webbook.filters.LoginAndPasswordAuthenticationFilter;
import com.webbook.webbook.filters.LogoutFilter;
import com.webbook.webbook.handlers.OAuth2LoginSuccessHandler;
import com.webbook.webbook.providers.LoginAndPasswordAuthenticationProvider;
import com.webbook.webbook.providers.TokenAuthenticationProvider;
import com.webbook.webbook.services.OAuth2UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("#{'${cors.allow.origins}'.split(',')}")
    private List<String> corsAllowOrigins;

    private final TokenAuthenticationProvider tokenAuthenticationProvider;
    private final LoginAndPasswordAuthenticationProvider loginAndPasswordAuthenticationProvider;
    private final OAuth2UserService auth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Lazy
    public SecurityConfig(OAuth2UserService auth2UserService,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                          TokenAuthenticationProvider tokenAuthenticationProvider,
                          LoginAndPasswordAuthenticationProvider loginAndPasswordAuthenticationProvider) {
        this.auth2UserService = auth2UserService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
        this.loginAndPasswordAuthenticationProvider = loginAndPasswordAuthenticationProvider;
    }

//    @Autowired
//    public void setTokenAuthenticationProvider(TokenAuthenticationProvider tokenAuthenticationProvider) {
//        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
//    }
//
//    @Autowired
//    public void setLoginAndPasswordAuthenticationProvider(LoginAndPasswordAuthenticationProvider loginAndPasswordAuthenticationProvider) {
//        this.loginAndPasswordAuthenticationProvider = loginAndPasswordAuthenticationProvider;
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public LoginAndPasswordAuthenticationFilter loginAndPasswordAuthenticationFilter() throws Exception {
        return new LoginAndPasswordAuthenticationFilter(authenticationManagerBean());
    }
    @Bean
    public JwtAccessTokenFilter getJwtAccessTokenFilter() throws Exception {
        return new JwtAccessTokenFilter(authenticationManagerBean());
    }

    @Bean
    public JwtRefreshTokenFilter getJwtRefreshTokenFilter() throws Exception {
        return new JwtRefreshTokenFilter(authenticationManagerBean());
    }

    @Bean
    public LogoutFilter getLogoutFilter() throws Exception {
        return new LogoutFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAndPasswordAuthenticationProvider)
                .authenticationProvider(tokenAuthenticationProvider);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //Add cors for frontEnd servers
        http.csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/login","/enroll").permitAll()
                .antMatchers(HttpMethod.GET,"/activate","/refresh","/test").permitAll()
                .antMatchers("/","/oauth2/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterAt(loginAndPasswordAuthenticationFilter(),BasicAuthenticationFilter.class)
                .addFilterAfter(getJwtRefreshTokenFilter(),LoginAndPasswordAuthenticationFilter.class)
                .addFilterAfter(getJwtAccessTokenFilter(),JwtRefreshTokenFilter.class )
                .addFilterBefore(getLogoutFilter(),LoginAndPasswordAuthenticationFilter.class)
                .oauth2Login()
                .userInfoEndpoint()
                .userService(auth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler);

    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsAllowOrigins);
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
         configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type","User-Agent","Refresh"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }







}
