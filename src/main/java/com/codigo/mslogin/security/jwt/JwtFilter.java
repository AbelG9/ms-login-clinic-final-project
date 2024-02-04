package com.codigo.mslogin.security.jwt;

import com.codigo.mslogin.security.CustomerDetailService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    private String username;
    private Claims claims;
    @Autowired
    CustomerDetailService customerDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().matches("/v1/users/signUp|/v1/users/login|/v1/persons")){
            filterChain.doFilter(request, response);
        }
        else{
            String authorization = request.getHeader("Authorization");
            String token = null;
            if(authorization!=null && authorization.startsWith("Bearer ")){
                token = authorization.substring(7);
                username = jwtUtil.getUsername(token);
                claims = jwtUtil.getAllClaims(token);
            }
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = customerDetailService.loadUserByUsername(username);
                if(jwtUtil.validateToken(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
                else{
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token expiro o es invalido");
                }
            }
            else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Username invalido");
            }
            filterChain.doFilter(request, response);
        }
    }
}
