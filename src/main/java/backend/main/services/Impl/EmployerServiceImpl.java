package backend.main.services.Impl;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRequest;
import backend.main.dto.response.EmployerLoginResponse;
import backend.main.entities.Employer;
import backend.main.enums.ErrorCode;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.EmployerRepository;
import backend.main.services.EmployerService;
import backend.main.utils.SendEmailHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class EmployerServiceImpl implements EmployerService {
    EmployerRepository employerRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    SendEmailHandler sendEmailHandler;

    public EmployerServiceImpl(EmployerRepository employerRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, SendEmailHandler sendEmailHandler) {
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.sendEmailHandler = sendEmailHandler;
    }

    private String generateEmployerID() {
       return "EMPL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    //dang ky
    @Override
    public Employer register(EmployerRequest employerRequest) {
        if(employerRepository.findByEmail(employerRequest.getEmail()).isPresent()){
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Employer employer = new Employer();

        employer.setEmployerId(generateEmployerID());
        employer.setEmail(employerRequest.getEmail());
        employer.setPassword(passwordEncoder.encode(employerRequest.getPassword()));
        employer.setCreateAt(LocalDate.now());
        employer.setUpdateAt(LocalDate.now());
        employer.setRole(Role.ROLE_EMPLOYER);
        employer.setEnabled(false);

        String token = UUID.randomUUID().toString();
        employer.setVerificationToken(token);

        String verifyLink = "http://localhost:8080/company/verify?token=" + token;
        sendEmailHandler.sendVerificationEmail(employer.getEmail(), verifyLink);

        return employerRepository.save(employer);
    }

    @Override
    public Employer verifyEmployer(String token){
        Employer employer = employerRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_INVALID));

        employer.setUpdateAt(LocalDate.now());
        employer.setEnabled(true);
        employer.setVerificationToken(null);

        return employerRepository.save(employer);
    }

    //Dang nhap
    @Override
    public EmployerLoginResponse login(EmployerLoginRequest employerLoginRequest) {
        Employer employer = employerRepository.findByEmail(employerLoginRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_DOES_NOT_EXIST));

        //So sanh mat khau
        if(!passwordEncoder.matches(employerLoginRequest.getPassword(), employer.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        //kiem tra xem tai khoan da duoc kich hoat chua
        if(employer.getEnabled() == false){
            throw new AppException(ErrorCode.ACCOUNT_UNENABLED);
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

    @Override
    public Employer updateInfo(String id, EmployerRequest request) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYER_NOT_FOUND));

        employer.setCompanyName(request.getCompanyName());
        employer.setAddress(request.getAddress());
        employer.setPhone(request.getPhone());
        employer.setAvatar(request.getAvatar());
        employer.setUpdateAt(LocalDate.now());

        return employerRepository.save(employer);
    }
}
