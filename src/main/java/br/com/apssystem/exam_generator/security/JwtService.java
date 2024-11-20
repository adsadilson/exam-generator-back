package br.com.apssystem.exam_generator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.awt.desktop.ScreenSleepEvent;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtService {

    public static void main(String[] args) {
        System.out.println(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        System.out.println(new BCryptPasswordEncoder().encode("adilson"));
    }


    public static final String SECRET = "elEAnEZSLi8qbVhnHe3Cixkq7Ty2wMs6VVu8pjPZsNfrymC5pwvxTFMQmaGIKXCwGXsV47ImKLB9W42bqoy7R8L49fAH233yOkiIhQVSqmPceHduTzrzy0tv6R2YKUlm4tVb7ERnOwnT2qdJOt8cjpvd7n2F5bSXsDXp5CBJZOsP3uoHLlHUA2YvdO17NHU58Fi38JCVmVyddMKOVfwk7iD8Ws5xZOsXhBQd8hYi0ml3XTHGY3QTckQWCLkqnAdtZ";
    public static final long EXPIRANTION_TIME = 1000 * 60 * 1; // 1 minuto

    public static String createSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] secretBytes = new byte[36]; //36*8=288 (>256 bits required for HS256)
        secureRandom.nextBytes(secretBytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(secretBytes);
    }

    public String extrairUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extrairExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extrairExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extrairUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRANTION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
