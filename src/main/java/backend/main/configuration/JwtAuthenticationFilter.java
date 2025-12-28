package backend.main.configuration;

import backend.main.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String authHeader = request.getHeader("Authorization");

        // 1. Ưu tiên lấy từ Header Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } 
        // 2. Nếu không có Header, tìm trong Cookie
        else if (request.getCookies() != null) {
            // Tìm cookie 'nimda' (Admin) trước
            for (Cookie cookie : request.getCookies()) {
                if ("nimda".equals(cookie.getName())) {
                    String tempToken = cookie.getValue();
                    if (jwtUtils.validateToken(tempToken)) {
                        token = tempToken;
                        break; // Tìm thấy token Admin hợp lệ thì dừng luôn
                    }
                }
            }

            // Nếu chưa tìm thấy token hợp lệ từ 'nimda', tìm tiếp 'jwt' (User/Employer)
            if (token == null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwt".equals(cookie.getName())) {
                        String tempToken = cookie.getValue();
                        if (jwtUtils.validateToken(tempToken)) {
                            token = tempToken;
                            break; // Tìm thấy token User hợp lệ thì dừng
                        }
                    }
                }
            }
        }

        // 3. Xác thực nếu có token hợp lệ
        if (token != null && jwtUtils.validateToken(token)) {
            String email = jwtUtils.extractEmail(token);
            String id = jwtUtils.extractId(token);
            String role = jwtUtils.extractRole(token);

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.singleton(authority));

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            request.setAttribute("userId", id);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
