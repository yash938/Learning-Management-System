package com.LMS.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtHelper {

    // Token validity duration (30 minutes)
    public static final long TOKEN_VALID = 30 * 60 * 1000;

    // Secret key for generating and validating token
    public static final String SECRET_KEY = "yashsharmayashsharmayashsharmayashashsarmajskldjljjklcmklnlisjdlkclkxmc";

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    // Key instance for signing the token
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    // Retrieve username from token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve a specific claim from the token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Retrieve all claims from the token
    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key) // Use verifyWith for token validation
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("Failed to parse token: {}", e.getMessage());
            throw new RuntimeException("Invalid token", e);
        }
    }

    // Check if the token is expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    // Retrieve the expiration date from the token
    public Date getExpirationFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Generate a new token for the given user details
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername()); // Add custom claims if needed
        return Jwts.builder()
                .claims(claims) // Set custom claims
                .subject(userDetails.getUsername()) // Set the subject (username)
                .issuedAt(new Date(System.currentTimeMillis())) // Set the issue time
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALID)) // Set the expiration time
                .signWith(key, SignatureAlgorithm.HS256) // Sign the token with the key
                .compact(); // Compact the token into a string
    }

    // Validate the token against the user details
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}