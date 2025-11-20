package backend.main.services.Impl;

import backend.main.configuration.JwtUtils;
import backend.main.dto.response.AdminResponse;
import backend.main.dto.response.CandidateResponse;
import backend.main.dto.response.EmployerResponse;
import backend.main.entities.Admin;
import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.enums.Code;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.AdminRepository;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
import backend.main.services.AuthService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static backend.main.enums.Role.ROLE_EMPLOYER;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    AdminRepository adminRepository;

    @Override
    @Transactional
    public Object checkToken(String token) {
        if (token == null || !jwtUtils.validateToken(token)) {
            throw new AppException(Code.TOKEN_INVALID);
        }
        //Lay id tu token
        System.out.println("Token:" + token);
        String email = jwtUtils.extractEmail(token);
        System.out.println("email:" + email);

        //lay role tu token
        String role = jwtUtils.extractRole(token);
        System.out.println("role:" + role);
//        ROLE_ADMIN


        switch (role) {
            case "ROLE_EMPLOYER" -> {
                return employerRepository.findByEmail(email)
                        .map(e -> new EmployerResponse(
                                e.getCompanyName(),
                                e.getCity(),
                                e.getAddress(),
                                e.getCompanyModel(),
                                e.getCompanyEmployees(),
                                e.getWorkingTime(),
                                e.getWorkingOvertime(),
                                e.getDescription(),


                                e.getPhone(),
                                e.getLogo(),
                                e.getRole()
                        )).orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));
            }
            case "ROLE_CANDIDATE" ->{
                return candidateRepository.findByEmail(email)
                        .map(c -> new CandidateResponse(
                                c.getFullname(),
                                c.getEmail(),
                                c.getAddress(),
                                c.getDateOfBirth(),
                                c.getPhone(),
                                c.getAvatar(),
                                c.getCv(),
                                c.getIsPrivate(),
                                c.getRole(),
                                c.getGender()
                        )).orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));
            }
            case "ROLE_ADMIN" ->{
                return adminRepository.findByEmail(email)
                        .map(a -> new AdminResponse(
                                a.getEmail(),
                                a.getName(),
                                a.getRole()
                        )).orElseThrow(() -> new AppException(Code.USER_NOT_FOUND));
            }
            default -> throw new AppException(Code.USER_NOT_FOUND);
        }
    }
}
