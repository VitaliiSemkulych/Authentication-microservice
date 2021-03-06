package com.webbook.resourceserver.security;



import com.webbook.resourceserver.filters.AuthenticationServerConnectFilter;
import com.webbook.resourceserver.filters.JwtAccessTokenFilter;
import com.webbook.resourceserver.providers.ResourceServerAuthenticationProvider;
import com.webbook.resourceserver.providers.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${resourceServer.url}")
    private String resourceServerURl;
    @Value("${frontendServer.url}")
    private String frontendServerURl;

    private TokenAuthenticationProvider tokenAuthenticationProvider;
    private ResourceServerAuthenticationProvider resourceServerAuthenticationProvider;



    @Autowired
    public void setTokenAuthenticationProvider(TokenAuthenticationProvider tokenAuthenticationProvider) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }
    @Autowired
    public void setResourceServerAuthenticationProvider(ResourceServerAuthenticationProvider resourceServerAuthenticationProvider) {
        this.resourceServerAuthenticationProvider = resourceServerAuthenticationProvider;
    }




    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAccessTokenFilter jwtAccessTokenFilter() throws Exception {
        return new JwtAccessTokenFilter(authenticationManagerBean());
    }

    @Bean
    public AuthenticationServerConnectFilter authenticationServerConnectFilter() throws Exception {
        return new AuthenticationServerConnectFilter(authenticationManagerBean());
    }


    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(tokenAuthenticationProvider)
                .authenticationProvider(resourceServerAuthenticationProvider);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        //Add cors for auth server
        http.csrf().disable().cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated().and()
                .addFilterAt(jwtAccessTokenFilter(),BasicAuthenticationFilter.class)
                .addFilterAfter(authenticationServerConnectFilter(),JwtAccessTokenFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendServerURl,resourceServerURl));
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type","User-Agent","Refresh"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}
