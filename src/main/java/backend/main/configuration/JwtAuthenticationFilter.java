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
        // Lấy header 'Authorization' từ nơi chưa JWT
        String authHeader = request.getHeader("Authorization");
        String token = null;

        //Ưu tiên lấy từ header Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }else {
            //Nếu không có header thì lấy ở cookie
            if(request.getCookies() != null){
                for(Cookie cookie: request.getCookies()){
                    if("jwt".equals(cookie.getName())){
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if(token != null && jwtUtils.validateToken(token)) {
            String email = jwtUtils.extractEmail(token);
            String id  = jwtUtils.extractId(token);
            String role  = jwtUtils.extractRole(token);

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