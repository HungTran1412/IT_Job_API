package backend.main.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.main.entities.Admin;
import backend.main.entities.User;
import backend.main.enums.Role;
import backend.main.repository.AdminRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminInit {
	
	PasswordEncoder encoder;

	@Bean
	ApplicationRunner applicationRunner(AdminRepository repository) {
		return args -> {
			if(repository.findByEmail("admin@dev.com").isEmpty()) {
				
				User user = Admin.builder()
								.name("admin")
								.password(encoder.encode("admin"))
								.email("dumabao69@gmail.com")
								.role(Role.ROLE_ADMIN)
								.build();
						
				repository.save((Admin) user);
				
				log.warn("created default admin");
			}
		};
	}

}

