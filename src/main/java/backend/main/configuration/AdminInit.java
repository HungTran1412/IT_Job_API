package backend.main.configuration;

import java.util.List;

import backend.main.dto.request.EmployerSubscriptionRequest;
import backend.main.entities.VipPackage;
import backend.main.repository.VipPackageRepository;
import backend.main.services.EmployerSubscriptionService;
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
	ApplicationRunner applicationRunner(AdminRepository repository,
                                        CandidateRepository candidateRepo,
                                        EmployerRepository employerRepo,
                                        VipPackageRepository vipPackageRepo,
                                        EmployerSubscriptionService employerSubscriptionService) {
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
                        .password(encoder.encode("Abc@1234"))
                        .email("candidate1@dev.com")
                        .role(Role.ROLE_CANDIDATE)
                        .enabled(true)
                        .isPrivate(false)
                        .isLocked(false)
                        .build();

                candidateRepo.save((Candidate) user);

                log.warn("created default candidate");
            }
            
            // Tạo gói VIP DEFAULT nếu chưa tồn tại
            VipPackage defaultVipPackage = vipPackageRepo.findByCode("DEFAULT").orElseGet(() -> {
                VipPackage newPackage = VipPackage.builder()
                        .code("DEFAULT")
                        .name("Gói Mặc Định")
                        .price(0.0)
                        .durationDays(36500) // 100 years
                        .postLimit(1000) // Giới hạn tổng
                        .weeklyPostLimit(3) // Giới hạn tuần
                        .jobPostDurationDays(7) // Thời hạn tin đăng
                        .description("Gói miễn phí dành cho nhà tuyển dụng mới đăng ký.")
                        .isActive(true)
                        .build();
                log.warn("Created DEFAULT VIP package.");
                return vipPackageRepo.save(newPackage);
            });
            
            if(employerRepo.findByEmail("company@dev2.com").isEmpty()) {

                Employer user = Employer.builder()
                                .companyName("company")
								.employerId("demoEmployer")
                                .password(encoder.encode("Abc@1234"))
                                .email("company@dev2.com")
                                .city(
                                		List.of("Hà Nội", "Đà Nẵng")
                                )
                                .role(Role.ROLE_EMPLOYER)
                                .enabled(true)
                                .isLocked(false)
                                .build();

                Employer savedEmployer = employerRepo.save(user);
                log.warn("created default employer");

                // Gán gói DEFAULT cho tài khoản employer mặc định
                if (savedEmployer.getSubscriptions() == null || savedEmployer.getSubscriptions().isEmpty()) {
                    EmployerSubscriptionRequest subRequest = EmployerSubscriptionRequest.builder()
                            .employerId(savedEmployer.getEmployerId())
                            .vipPackageId(defaultVipPackage.getId())
                            .build();
                    employerSubscriptionService.createSubscription(subRequest);
                    log.warn("Assigned DEFAULT VIP package to default employer.");
                }
            }
        };
    }

}
