package backend.main.services.Impl;

import backend.main.configuration.JwtUtils;
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

        if (role.equals(Role.ROLE_EMPLOYER.name())) {
            return employerRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(Code.USER_NOT_FOUND));
        } else if (role.equals(Role.ROLE_CANDIDATE.name())) {
            return candidateRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(Code.USER_NOT_FOUND));
        } else if (role.equals(Role.ROLE_ADMIN.name())) {
            return adminRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(Code.USER_NOT_FOUND));
        }

        throw new AppException(Code.USER_NOT_FOUND);
    }
}
