package backend.main.services.Impl;

import backend.main.dto.request.CandidateRegisterRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.response.CandidateLoginResponse;
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
        do {
            int randomNumber = 100000 + random.nextInt(900000); // Sinh số từ 100000 đến 999999
            id = "USER" + randomNumber;
        } while (candidateRepository.existsById(id)); // Nếu ID đã tồn tại thì sinh lại
        return id;
    }

    @Override
    public Candidate register(CandidateRegisterRequest candidateRegisterRequest) {
        // Tạo đối tượng Candidate mới
        Candidate candidate = new Candidate();

        // Gán giá trị từ request sang entity
        candidate.setFullname(candidateRegisterRequest.getFullname());
        candidate.setCandidateId(generateCandidateID());
        candidate.setEmail(candidateRegisterRequest.getEmail());
        candidate.setPassword(passwordEncoder.encode(candidateRegisterRequest.getPassword())); // Mã hóa mật khẩu
        candidate.setGender(candidateRegisterRequest.getGender());
        candidate.setPhone(candidateRegisterRequest.getPhone());
        candidate.setDateOfBirth(candidateRegisterRequest.getDateOfBirth());
        candidate.setAddress(candidateRegisterRequest.getAddress());
        candidate.setAvatar(candidateRegisterRequest.getAvatar());
        candidate.setCreateAt(LocalDate.now()); // Ngày tạo tài khoản
        candidate.setUpdateAt(LocalDate.now()); // Ngày cập nhật tài khoản
        candidate.setCv(candidateRegisterRequest.getCv());
        candidate.setRole("CANDIDATE"); // Gán vai trò mặc định

        // Lưu vào database
        return candidateRepository.save(candidate);
    }

    @Override
    public CandidateLoginResponse login(CandidateLoginRequest candidateLoginRequest) {
        // Tìm ứng viên theo email, nếu không có thì ném lỗi
        Candidate candidate = candidateRepository.findByEmail(candidateLoginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found!"));

        // So sánh mật khẩu nhập vào với mật khẩu đã mã hóa trong DB
        if (!passwordEncoder.matches(candidateLoginRequest.getPassword(), candidate.getPassword())) {
            throw new RuntimeException("Wrong password!");
        }

        // Trả về thông tin ứng viên (không bao gồm mật khẩu)
        return new CandidateLoginResponse(
                candidate.getCandidateId(),
                candidate.getFullname(),
                candidate.getEmail(),
                candidate.getGender(),
                candidate.getAddress(),
                candidate.getDateOfBirth(),
                candidate.getCreateAt(),
                candidate.getUpdateAt(),
                candidate.getPhone(),
                candidate.getAvatar(),
                candidate.getRole()
        );
    }
}
