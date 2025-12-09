package backend.main.services.Impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.dto.response.AdminResponse;
import backend.main.dto.response.CandidateResponse;
import backend.main.dto.response.EmployerResponse;
import backend.main.entities.Job;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.AdminRepository;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
import backend.main.services.AuthService;
import backend.main.utils.JwtUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
@Slf4j
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
                var c = candidateRepository.findByEmail(email)
                        .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));
                
                List<String> likedIds = c.getLikedJobs() == null ? Collections.emptyList()
                	    : c.getLikedJobs().stream()
                	        .map(Job::getJobId) 
                	        .collect(Collectors.toList());
                
                List<String> appliedIds = c.getApplications() == null ? Collections.emptyList()
                	    : c.getApplications().stream()
                	        .map(app -> app.getJob())
                	        .map(Job::getJobId)
                	        .collect(Collectors.toList());
                
                return new CandidateResponse(
                        c.getCandidateId(),
                        c.getFullname(),
                        c.getEmail(),
                        c.getAddress(),
                        c.getDateOfBirth(),
                        c.getPhone(),
                        c.getAvatar(),
                        c.getCv(),
                        c.getIsPrivate(),
                        c.getRole(),
                        c.getGender(),
                        c.getExperience(),
                        c.getTechnologies(),
                        c.getSoftSkill(),
                        c.getDesiredSalary(),
                        likedIds,
                        appliedIds
                    );
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