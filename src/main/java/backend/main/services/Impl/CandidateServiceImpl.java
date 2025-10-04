package backend.main.services.Impl;

import backend.main.dto.request.CandidateRegisterRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.response.LoginResponse;
import backend.main.entities.Candidate;
import backend.main.repository.CandidateRepository;
import backend.main.services.CandidateService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Service
public class CandidateServiceImpl implements CandidateService {
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
    public Candidate register(CandidateRegisterRequest candidateRegisterRequest) {
        //Tao nguoi dung moi
        Candidate candidate = new Candidate();

        candidate.setFullname(candidateRegisterRequest.getFullname());
        candidate.setCandidateId(generateCandidateID());
        candidate.setEmail(candidateRegisterRequest.getEmail());
        candidate.setPassword(passwordEncoder.encode(candidateRegisterRequest.getPassword()));
        candidate.setGender(candidateRegisterRequest.getGender());
        candidate.setPhone(candidateRegisterRequest.getPhone());
        candidate.setDateOfBirth(candidateRegisterRequest.getDateOfBirth());
        candidate.setAddress(candidateRegisterRequest.getAddress());
        candidate.setAvatar(candidateRegisterRequest.getAvatar());
        candidate.setCreateAt(LocalDate.now());
        candidate.setUpdateAt(LocalDate.now());
        candidate.setCv(candidateRegisterRequest.getCv());
        candidate.setRole("CANDIDATE");

        return candidateRepository.save(candidate);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
       Candidate candidate = candidateRepository.findByEmail(loginRequest.getEmail())
               .orElseThrow(()-> new RuntimeException("Email not found!"));

       if(!passwordEncoder.matches(loginRequest.getPassword(),candidate.getPassword())){
           throw new RuntimeException("Wrong password!");
       }

       return new LoginResponse(candidate.getCandidateId(),
                               candidate.getFullname(),
                               candidate.getEmail(),
                               candidate.getGender(),
                               candidate.getAddress(),
                               candidate.getDateOfBirth(),
                               candidate.getCreateAt(),
                               candidate.getUpdateAt(),
                               candidate.getPhone(),
                               candidate.getAvatar(),
                               candidate.getRole());
    }
}
