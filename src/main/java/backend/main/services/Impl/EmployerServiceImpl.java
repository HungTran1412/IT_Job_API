package backend.main.services.Impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.configuration.AppProperties;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.employer.EmployerRegisterRequest;
import backend.main.dto.request.employer.EmployerUpdateRequest;
import backend.main.entities.Employer;
import backend.main.entities.Job;
import backend.main.entities.VerificationToken;
import backend.main.enums.Code;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.EmployerRepository;
import backend.main.repository.VerificationTokenRepository;
import backend.main.services.EmployerService;
import backend.main.utils.CloudinaryFileUpload;
import backend.main.utils.JwtUtils;
import backend.main.utils.SendEmailHandler;
import backend.main.utils.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
    CloudinaryFileUpload cloudinaryFileUpload;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    AppProperties appProperties;

    private String generateEmployerID() {
       return "EMPL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    //dang ky
    @Override
    @Transactional
    public Employer register(EmployerRegisterRequest employerRequest) {
        ValidationUtils.validateEmail(employerRequest.getEmail());
        ValidationUtils.validatePassword(employerRequest.getPassword());

        if(employerRepository.findByEmail(employerRequest.getEmail()).isPresent()){
            throw new AppException(Code.EMAIL_EXISTED);
        }

        Employer employer = Employer.builder()
                .employerId(generateEmployerID())
                .companyName(employerRequest.getCompanyName())
                .email(employerRequest.getEmail())
                .password(passwordEncoder.encode(employerRequest.getPassword()))
                .role(Role.ROLE_EMPLOYER)
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .build();

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
        String verifyLink = appProperties.getBaseUrl() + appProperties.getVerify().getEmployer()+ token;
        sendEmailHandler.sendVerificationEmail(employer.getEmail(), verifyLink);


        return employer;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        try {
            Employer e = employerRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

            if(e.getPassword() == null){
                throw new AppException(Code.PASSWORD_IS_NULL);
            }

            //kiểm tra mật khẩu cũ trước khi đổi
            if(!passwordEncoder.matches(oldPassword,e.getPassword())){
                throw new AppException(Code.OLD_PASSWORD_NOT_MATCH);
            }

            e.setPassword(passwordEncoder.encode(newPassword));
            saveEmployer(e);
            return true;
        } catch (AppException e) {
            return false;
        }
    }

    @Override
	@Transactional
    public void resendVerification(String email) {
        Employer e = employerRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

        if (e.getEnabled() == true) {
            throw new AppException(Code.ACCOUNT_VERIFIED);
        }

        // Xóa token cũ nếu đã tồn tại
        verificationTokenRepository.findByEmployer(e)
                .ifPresent(verificationTokenRepository::delete);

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        VerificationToken newToken = new VerificationToken();
        newToken.setToken(token);
        newToken.setEmployer(e);
        newToken.setExpirationTime(LocalDateTime.now().plusSeconds(30));

        verificationTokenRepository.save(newToken);

        String verifyLink = appProperties.getBaseUrl() + appProperties.getVerify().getEmployer() + token;
        sendEmailHandler.sendVerificationEmail(e.getEmail(), verifyLink);
    }

    //Dang nhap
    @Transactional
    @Override
    public String login(LoginRequest request) {
        ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validatePassword(request.getPassword());

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

        return jwtUtils.generateToken(e.getEmployerId(), e.getEmail(), e.getRole(), request.isRememberMe());
    }

    @Override
    @Transactional
    public Employer updateInfo(String id, EmployerUpdateRequest request) {
        Employer e = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));

        e.setCompanyName(request.getCompanyName());
        e.setAddress(request.getAddress());
        e.setPhone(request.getPhone());

        // Parse city from JSON string to List<String>
        if (request.getCity() != null && !request.getCity().isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> cityList = objectMapper.readValue(request.getCity(), new TypeReference<List<String>>() {});
                e.setCity(cityList);
            } catch (JsonProcessingException jsonException) {
                log.error("Error parsing city JSON string: {}. Setting city to empty list.", request.getCity(), jsonException);
                e.setCity(Collections.emptyList());
            }
        } else {
            e.setCity(Collections.emptyList());
        }

        e.setCompanyModel(request.getCompanyModel());
        e.setCompanyEmployees(request.getCompanyEmployees());
        e.setWorkingTime(request.getWorkingTime());
        e.setWorkingOvertime(request.getWorkingOvertime());
        e.setDescription(request.getDescription());
        e.setUpdateAt(LocalDateTime.now());

        //Kiem tra xem nguoi dung co cap nhat anh khong
        if(request.getLogo() != null && !request.getLogo().isEmpty()){
            String imgUrl = cloudinaryFileUpload.uploadImage(request.getLogo());
            e.setLogo(imgUrl);
        }

        return saveEmployer(e);
    }

    private Employer saveEmployer(Employer e) {
        e.setUpdateAt(LocalDateTime.now());
        return employerRepository.save(e);
    }
    
    @Override
    @Transactional
    public List<Job> getListJob(String jwt){
	   String id = jwtUtils.extractId(jwt);
       Employer employer = employerRepository.findById(id)
               .orElseThrow(() -> new AppException(Code.EMAIL_DOES_NOT_EXIST));
	   return employer.getJobs();
    }
}
