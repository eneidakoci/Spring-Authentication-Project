package com.joan.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joan.security.dto.CredentialsDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UsernameAndPasswordAuthFilter extends OncePerRequestFilter  {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final UserAuthenticationProvider provider;

    public UsernameAndPasswordAuthFilter(UserAuthenticationProvider provider){
        this.provider = provider;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/v1/auth/log-in".equals(request.getServletPath()) && HttpMethod.POST.matches(request.getMethod())) {
            CredentialsDTO credentialsDTO = MAPPER.readValue(request.getInputStream(),CredentialsDTO.class);

            try {
                SecurityContextHolder.getContext().setAuthentication(provider.validateCredentials(credentialsDTO));
            } catch (RuntimeException e){
                SecurityContextHolder.clearContext();
                throw e;
            }
        }

        filterChain.doFilter(request,response);
    }
}