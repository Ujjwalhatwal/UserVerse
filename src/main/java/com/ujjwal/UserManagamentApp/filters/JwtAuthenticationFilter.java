package com.ujjwal.UserManagamentApp.filters;

import com.ujjwal.UserManagamentApp.repository.UserRepository;
import com.ujjwal.UserManagamentApp.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Long userId = null;
        String jwtToken = null;
        final String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
        }

        // if jwt token is null, need to fetch the token from the http cookie
        if(jwtToken == null || jwtToken.isBlank()){

            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("JWT".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // if token still not found
        if(jwtToken == null || jwtToken.isBlank()){
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is missing or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract userId from the token
        userId = jwtUtil.extractUserId(jwtToken);

        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){

            var userDetails = userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("User not found.")
            );

            // validate the token
            if(jwtUtil.isTokenValid(userDetails,jwtToken)){
                List<SimpleGrantedAuthority> authorities = userDetails.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authToken =
                        new  UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }
}
