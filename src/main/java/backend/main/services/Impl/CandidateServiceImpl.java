package backend.main.services.Impl;

import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.response.LoginResponse;
import backend.main.entities.Candidate;
import backend.main.repository.CandidateRepository;
import backend.main.services.CandidateServices;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Service
public class CandidateServiceImpl implements CandidateServices {
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public CandidateServiceImpl(CandidateRepository candidateRepository, PasswordEncoder passwordEncoder) {
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String generateCandidateID() {
        Random random = new Random();
        String id;
        do{
            int randomNumber = 100000 + random.nextInt(900000);
            id = "USER" + randomNumber;
        }while(candidateRepository.existsById(id));

        return id;
    }

    @Override
    public Candidate register(CandidateRequest candidateRequest) {
        //Tao nguoi dung moi
        Candidate candidate = new Candidate();

        candidate.setFullname(candidateRequest.getFullname());
        candidate.setCandidateId(generateCandidateID());
        candidate.setEmail(candidateRequest.getEmail());
        candidate.setPassword(passwordEncoder.encode(candidateRequest.getPassword()));
        candidate.setPhone(candidateRequest.getPhone());
        candidate.setDateOfBirth(candidateRequest.getDateOfBirth());
        candidate.setAddress(candidateRequest.getAddress());
        candidate.setAvatar(candidateRequest.getAvatar());
        candidate.setCreateAt(LocalDate.now());
        candidate.setUpdateAt(LocalDate.now());

        return candidateRepository.save(candidate);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
       Candidate candidate = candidateRepository.findByEmail(loginRequest.getEmail())
               .orElseThrow(()-> new RuntimeException("Email not found!"));

       if(!passwordEncoder.matches(loginRequest.getPassword(),candidate.getPassword())){
           throw new RuntimeException("Wrong password!");
       }

       return new LoginResponse(candidate.getCandidateId(), candidate.getEmail(), "Login Successful!");
    }
}
