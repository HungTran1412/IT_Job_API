package backend.main.configuration;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.main.enums.Role;
import backend.main.repository.AdminRepository;
import backend.main.entities.User;

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
				Set<String> roles = new HashSet<String>();
				roles.add(Role.ROLE_ADMIN.name());
				
				User user = Admin.builder()
								.name("admin")
								.password(encoder.encode("admin"))
								.email("dumabao69@gmail.com")
								.roles(roles)
								.build();
						
				repository.save(user);
				
				log.warn("created default admin");
			}
		};
	}

}

