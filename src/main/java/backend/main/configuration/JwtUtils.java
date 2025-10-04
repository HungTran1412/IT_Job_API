package backend.main.configuration;

import backend.main.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtUtils {
    //Lấy secret key ở file application.properties
    @Value("${jwt.secret}")
    String secretKey;

    //Lấy thời gian hết hạn
    @Value("${jwt.expiration}")
    long expirationMs;

    //Tạo khóa bí mật từ secret key
    Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    //Tạo JWT mới
    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Hàm kiểm tra tính hợp lệ của token
    public boolean validateToken(String token) {
        try {
            //Xác minh chữ ký
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    //Trích xuất email ở subject từ token
    public String extractEmail(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    //Trich xuat role
    public String extractRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}
