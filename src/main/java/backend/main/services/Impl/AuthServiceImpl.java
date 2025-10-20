package backend.main.services.Impl;

import backend.main.configuration.JwtUtils;
import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
import backend.main.services.AuthService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    CandidateRepository candidateRepository;

    @Override
    public Object checkToken(String token) {
        if(token == null || !jwtUtils.validateToken(token)) {
            throw new AppException(Code.TOKEN_INVALID);
        }
        System.out.println("Token:"+token);
        String id = jwtUtils.extractId(token);
        System.out.println("id:"+id);

        String str = id.substring(0,4);

        System.out.println("str:"+str);
        if(str.equals("EMPL")){
            Employer e = employerRepository.findById(id)
                    .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));
            return e;
        }else if(str.equals("USER")){
            Candidate c  = candidateRepository.findById(id)
                    .orElseThrow(() -> new AppException(Code.USER_NOT_FOUND));
            return c;
        }

        throw new AppException(Code.USER_NOT_FOUND);
    }
}
