package com.example.demo.appuser.registration.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class TokenFilter extends BasicAuthenticationFilter {
    private ConfTokenService confTokenService;

    public TokenFilter(AuthenticationManager authenticationManager, ConfTokenService confTokenService) {
        super(authenticationManager);
        this.confTokenService = confTokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/logs")) {
            Optional<ConfToken> confToken = confTokenService.getToken(request.getHeader("Authorization"));

            if (confToken.isPresent()) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        confToken.get().getAppUser(), null, confToken.get().getAppUser().getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                new ObjectMapper().writeValue(response.getOutputStream(), "Invalid Token");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
