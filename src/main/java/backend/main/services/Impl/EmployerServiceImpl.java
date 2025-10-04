package backend.main.services.Impl;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRequest;
import backend.main.dto.response.EmployerLoginResponse;
import backend.main.entities.Employer;
import backend.main.enums.Role;
import backend.main.repository.EmployerRepository;
import backend.main.services.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class EmployerServiceImpl implements EmployerService {
    @Autowired
    EmployerRepository employerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    private String generateEmployerID() {
        Random random = new Random();
        String id;
        do{
            int randomNumber = 100000 + random.nextInt(900000);
            id = "EMPL" + randomNumber;
        }while(employerRepository.existsById(id));

        return id;
    }

    //dang ky
    @Override
    public Employer register(EmployerRequest employerRequest) {
        Employer employer = new Employer();

        employer.setEmployerId(generateEmployerID());
        employer.setEmail(employerRequest.getEmail());
        employer.setPassword(passwordEncoder.encode(employerRequest.getPassword()));
        employer.setCompanyName(employerRequest.getCompanyName());
        employer.setAddress(employerRequest.getAddress());
        employer.setPhone(employerRequest.getPhone());
        employer.setAvatar(employerRequest.getAvatar());
        employer.setCreateAt(LocalDate.now());
        employer.setUpdateAt(LocalDate.now());
        employer.setRole(Role.ROLE_EMPLOYER);

        return employerRepository.save(employer);
    }

    //Dang nhap
    @Override
    public EmployerLoginResponse login(EmployerLoginRequest employerLoginRequest) {
        Employer employer = employerRepository.findByEmail(employerLoginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found!"));

        //So sanh mat khau
        if(!passwordEncoder.matches(employerLoginRequest.getPassword(), employer.getPassword())) {
            throw new RuntimeException("Wrong password!");
        }

        String token = jwtUtils.generateToken(employer.getEmail(), employer.getRole());

        return new EmployerLoginResponse(employer.getEmployerId(),
                                         employer.getCompanyName(),
                                         employer.getEmail(),
                                         employer.getAddress(),
                                         employer.getPhone(),
                                         employer.getCreateAt(),
                                         employer.getUpdateAt(),
                                         employer.getAvatar(),
                                         employer.getRole(),
                                         token);
    }


}
