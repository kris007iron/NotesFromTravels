package com.kris007iron.NotesFromTravels.Auth;

import com.kris007iron.NotesFromTravels.Service.JwtService;
import com.kris007iron.NotesFromTravels.Service.TokenService;
import com.kris007iron.NotesFromTravels.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Skip filtering if authentication is already set
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String role = JwtService.extractRoleFromAuth(request, userService);
            if(request.getRequestURL().toString().startsWith("http://localhost:8080/api/v1/admin") && role.equals("USER")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized for this panel");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
            String token = authHeader.substring(7);
            String login = JwtService.getLoginFromToken(token);

            if (login != null && !login.isEmpty()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(login);
                if (JwtService.validateToken(token, userDetails) && !tokenService.isTokenExpired(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    String role = JwtService.extractRoleFromAuth(request, userService);
                    if(request.getRequestURL().toString().startsWith("http://localhost:8080/api/v1/admin") && role.equals("USER")) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized for this panel");
                        return;
                    }
                    response.addHeader("Authorization", JwtService.generateToken(userDetails.getUsername(), 30000));
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                    return;
                }
            } else{
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}