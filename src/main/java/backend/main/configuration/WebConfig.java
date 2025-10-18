package backend.main.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean  // @Bean: Định nghĩa một tiêu chí cấu hình (qua một method)
    public WebMvcConfigurer corsConfigurer() {
        // Implement trực tiếp interface WebMvcConfigurer
        // (Implement và tạo luôn đối tượng)
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://9608c3dae040.ngrok-free.app/", "http://localhost:3000/") // Cho phép ứng dụng Vue khác cổng truy cập
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}