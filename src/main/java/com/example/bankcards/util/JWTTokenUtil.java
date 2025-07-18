package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implements service for creating and processing with JWT tokens
 */
public class JWTTokenService {
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Long accessTokenExpiration = 3600000L;
    private final Long refreshTokenExpiration = 5184000000L;

    /**
     * The method allows to generate new JWT token
     *
     * @param userDetails - details of the current user from the request
     * @return new JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * The method allows to generate new refresh token
     *
     * @param userDetails - details of the current user from the request
     * @return new refresh token
     */
    public String generateRefreshToken(final UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * The method allows to get expiration of the refresh token
     *
     * @return expiration of the refresh token
     */
    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    /**
     * The method allows to extract current user's name from the token
     *
     * @param token - token with current user's data
     * @return - current user's name
     */
    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * The method allows to extract expiration of the current token
     *
     * @param token - current token with user's data
     * @return token's expiration
     */
    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * The method allows to extract claim from the token
     *
     * @param token          - token with current user's data
     * @param claimsResolver - function to get data
     * @param <T>            - type of the claim resolver result
     * @return claim from the current token
     */
    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * The method allows to extract all claims from the token
     *
     * @param token - token with data
     * @return claim
     */
    private Claims extractAllClaims(final String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * The method allows to validate current token
     *
     * @param token       - current token
     * @param userDetails - current user's data
     * @return is current token valid
     */
    public boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * The method allows to check is current token expired
     *
     * @param token - current token
     * @return is current token expired
     */
    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }
}