package backend.main.configuration;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.main.entities.Admin;
import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.entities.User;
import backend.main.enums.Role;
import backend.main.repository.AdminRepository;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
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
	AppProperties appProperties;

	@Bean
	ApplicationRunner applicationRunner(AdminRepository repository,CandidateRepository candidateRepo,EmployerRepository employerRepo) {
        return args -> {
            if(repository.findByEmail(appProperties.getAdmin().getEmail()).isEmpty()) {

                User user = Admin.builder()
                                .name("admin")
                                .password(encoder.encode(appProperties.getAdmin().getPassword()))
                                .email(appProperties.getAdmin().getEmail())
                                .role(Role.ROLE_ADMIN)
                                .build();

                repository.save((Admin) user);

                log.warn("created default admin");
            }
            if(candidateRepo.findByEmail("candidate1@dev.com").isEmpty()) {

                User user = Candidate.builder()
                        .fullname("candidate")
						.candidateId("demoCandicate")
                        .password(encoder.encode("Candidate@123"))
                        .email("candidate1@dev.com")
                        .role(Role.ROLE_CANDIDATE)
                        .enabled(true)
                        .isPrivate(false)
                        .build();

                candidateRepo.save((Candidate) user);

                log.warn("created default admin");
            }
            if(employerRepo.findByEmail("company@dev2.com").isEmpty()) {

                User user = Employer.builder()
                                .companyName("company")
								.employerId("demoEmployer")
                                .password(encoder.encode("Company@123"))
                                .email("company@dev2.com")
                                .city(
                                		List.of("Hà Nội", "Đà Nẵng")
                                )
                                .role(Role.ROLE_EMPLOYER)
                                .enabled(true)
                                .isLocked(false)
                                .build();

                employerRepo.save((Employer) user);

                log.warn("created default admin");
            }
        };
    }

}
