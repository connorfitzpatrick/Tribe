package com.friendsocial.Backend.config;

import com.friendsocial.Backend.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  private final TokenBlacklistService tokenBlacklistService;

  @Autowired
  public JwtService(TokenBlacklistService tokenBlacklistService) {
    this.tokenBlacklistService = tokenBlacklistService;
  }

  public Long extractUserId(UserDetails userDetails) {
    // Assuming the userId is a Long type property of your UserDetails implementation
    return ((User) userDetails).getId();
  }

  public String extractHandle(UserDetails userDetails) {
    // Assuming the userId is a Long type property of your UserDetails implementation
    return ((User) userDetails).getHandle();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // If you want to generate token without claims
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  // Main generateToken
  public String generateToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails
  ) {
    Long userId = extractUserId(userDetails);
    String handle = extractHandle(userDetails);
    return Jwts.builder()
            .setClaims(extraClaims)
            .claim("userId", userId)
            .claim("handle", handle)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 30 min
//            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public String generateRefreshToken(
          UserDetails userDetails
  ) {
    // 12 hours in milliseconds
    long refreshTokenExpirationMs =  12 * 60 * 60 * 1000;

    return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    // check if username from token matches the one stored in DB, if token is expired, and if it is blacklisted;
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && !tokenBlacklistService.isBlacklisted(token);
  }

  // Public method to check if the token is expired
  public boolean isTokenExpired(String token) {
    return isTokenExpiredInternal(token);
  }

  private boolean isTokenExpiredInternal(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  private Key getSignInKey() {
    String SECRET_KEY = ConfigLoader.getSecretKey();
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
