package com.evgenygerasimov.task_manager_rest.security;


import com.evgenygerasimov.task_manager_rest.constants.TokenLifeTime;
import com.evgenygerasimov.task_manager_rest.entity.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${myapp.secret.key}")
    private String secretKey;
    private final long validityTenMinutes = TokenLifeTime.TEN_MINUTES.getMinutes();
    private final long validityThirtyMinutes = TokenLifeTime.THIRTY_MINUTES.getMinutes();
    @Autowired
    private TokenRepository tokenRepository;

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validityTenMinutes))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validityThirtyMinutes))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void invalidateToken(String accessToken) {
        Optional<Token> tokenOpt = tokenRepository.findByAccessToken(accessToken);
        if (tokenOpt.isPresent()) {
            Token token = tokenOpt.get();
            token.setValid(false);
            tokenRepository.save(token);
        } else {
            Optional<Token> refreshTokenOpt = tokenRepository.findByRefreshToken(accessToken);
            if (refreshTokenOpt.isPresent()) {
                Token refreshToken = refreshTokenOpt.get();
                refreshToken.setValid(false);
                tokenRepository.save(refreshToken);
            }
        }
    }

    public void saveToken(String username, String accessToken, String refreshToken) {
        Token token = new Token();
        token.setUsername(username);
        token.setAccessToken(accessToken);
        token.setValid(true);
        token.setRefreshToken(refreshToken);
        token.setValid(true);
        tokenRepository.save(token);
    }

    public Token getAccessToken(String token) {
        Token tokenObj = null;
        if (token.startsWith("Bearer ")) {
            tokenObj = tokenRepository.findByAccessToken(token.substring(7)).get();
        } else {
            tokenObj = tokenRepository.findByAccessToken(token).get();
        }
        return tokenObj;
    }

    public List<Token> getTokens() {
        return tokenRepository.findAll();
    }
}