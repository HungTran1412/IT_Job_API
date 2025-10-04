package backend.main.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class )
                .authorizeHttpRequests(auth -> auth
                        // Cho phép không cần token
                        .requestMatchers("/company/login", "/user/login", "/company/register", "/user/register").permitAll()

                        // Các API dành riêng cho employer
                        .requestMatchers("/company/**").hasAuthority("ROLE_EMPLOYER")

                        // Các API dành riêng cho candidate
                        .requestMatchers("/user/**").hasAuthority("ROLE_CANDIDATE")

                        // Các request khác cần xác thực
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
