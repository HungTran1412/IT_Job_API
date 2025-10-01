package backend.main.services.Impl;

import backend.main.dto.request.CandidateRequest;
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
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
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
}
