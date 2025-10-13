package backend.main.services.Impl;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRequest;
import backend.main.entities.Employer;
import backend.main.entities.VerificationToken;
import backend.main.enums.Code;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.EmployerRepository;
import backend.main.repository.VerificationTokenRepository;
import backend.main.services.EmployerService;
import backend.main.utils.CloudinaryImageUpload;
import backend.main.utils.SendEmailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmployerServiceImpl implements EmployerService {
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    SendEmailHandler sendEmailHandler;
    @Autowired
    CloudinaryImageUpload  cloudinaryImageUpload;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;

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
            throw new AppException(Code.EMAIL_EXISTED);
        }

        Employer employer = new Employer();

        employer.setEmployerId(generateEmployerID());
        employer.setCompanyName(employerRequest.getCompanyName());
        employer.setEmail(employerRequest.getEmail());
        employer.setPassword(passwordEncoder.encode(employerRequest.getPassword()));
        employer.setCreateAt(LocalDateTime.now());
        employer.setUpdateAt(LocalDateTime.now());
        employer.setRole(Role.ROLE_EMPLOYER);
        employer.setEnabled(false);

        //luu thong tin
        saveEmployer(employer);

        //sinh token ngau nhien
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        verificationToken.setEmployer( employer );

        verificationTokenRepository.save(verificationToken);

        //gan link + token vua sinh, gui email
        String verifyLink = "http://localhost:8080/company/verify?token=" + token;
        sendEmailHandler.sendVerificationEmail(employer.getEmail(), verifyLink);


        return employer;
    }

    @Override
    public Employer verifyEmployer(String token){
        VerificationToken vt = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(Code.TOKEN_INVALID));

        if(vt.getExpirationTime().isBefore(LocalDateTime.now())){
            Employer e = vt.getEmployer();
            verificationTokenRepository.delete(vt);
            employerRepository.delete(e);
            throw new AppException(Code.TOKEN_EXPIRED);
        }

        Employer e = vt.getEmployer();
        e.setUpdateAt(LocalDateTime.now());
        e.setEnabled(true);
        verificationTokenRepository.delete(vt);
        return saveEmployer(e);
    }

    //Dang nhap
    @Override
    public String login(EmployerLoginRequest request) {
        Employer e = employerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(Code.EMAIL_DOES_NOT_EXIST));

        //So sanh mat khau
        if(!passwordEncoder.matches(request.getPassword(), e.getPassword())) {
            throw new AppException(Code.WRONG_PASSWORD);
        }

        //kiem tra xem tai khoan da duoc kich hoat chua
        if(e.getEnabled() == false){
            throw new AppException(Code.ACCOUNT_UNENABLED);
        }

        return jwtUtils.generateToken(e.getEmployerId(), e.getEmail(), e.getRole());
    }

    @Override
    public Employer updateInfo(String id, EmployerRequest request) {
        Employer e = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));

        e.setCompanyName(request.getCompanyName());
        e.setAddress(request.getAddress());
        e.setPhone(request.getPhone());
        e.setUpdateAt(LocalDateTime.now());

        //Kiem tra xem nguoi dung co cap nhat anh khong
        if(request.getAvatar() != null && !request.getAvatar().isEmpty()){
            System.out.println("Image: " + request.getAvatar().getOriginalFilename());
            String imgUrl = cloudinaryImageUpload.uploadImage(request.getAvatar());
            e.setAvatar(imgUrl);
        }

        return saveEmployer(e);
    }

    private Employer saveEmployer(Employer e) {
        e.setUpdateAt(LocalDateTime.now());
        return employerRepository.save(e);
    }
}
