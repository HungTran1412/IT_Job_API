package backend.main.services.Impl;

import backend.main.dto.request.EmployerRegisterRequest;
import backend.main.entities.Employer;
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

    private String generateEmployerID() {
        Random random = new Random();
        String id;
        do{
            int randomNumber = 100000 + random.nextInt(900000);
            id = "EMPL" + randomNumber;
        }while(employerRepository.existsById(id));

        return id;
    }

    @Override
    public Employer register(EmployerRegisterRequest employerRegisterRequest) {
        Employer employer = new Employer();

        employer.setEmployerId(generateEmployerID());
        employer.setEmail(employerRegisterRequest.getEmail());
        employer.setPassword(passwordEncoder.encode(employerRegisterRequest.getPassword()));
        employer.setCompanyName(employerRegisterRequest.getCompanyName());
        employer.setAddress(employerRegisterRequest.getAddress());
        employer.setPhone(employerRegisterRequest.getPhone());
        employer.setAvatar(employerRegisterRequest.getAvatar());
        employer.setCreateAt(LocalDate.now());
        employer.setUpdateAt(LocalDate.now());
        employer.setRole("EMPLOYER");

        return employer;
    }
}
