package com.LMS.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        logger.info("Header: {}", authorizationHeader);

        String username = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Extract the token after "Bearer "

            try {
                username = jwtHelper.getUsernameFromToken(token);
                logger.info("Token username: {}", username);
            } catch (IllegalArgumentException e) {
                logger.error("Illegal argument while fetching the username from token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.error("Given JWT token is expired: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                logger.error("Some changes were made to the token: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("An unexpected error occurred: {}", e.getMessage());
            }
        } else {
            logger.warn("Invalid Authorization header");
        }

        // If username is not null and no authentication is set in the SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtHelper.validateToken(token, userDetails)) {
                // Create an authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}