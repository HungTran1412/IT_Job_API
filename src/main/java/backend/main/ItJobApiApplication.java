package backend.main;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8080/", description = "Local"),
                @Server(url = "https://c9d8ae113937.ngrok-free.app", description = "Ngrok tunnel")
        }
)
@EnableScheduling
@SpringBootApplication
public class ItJobApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItJobApiApplication.class, args);
    }

}
