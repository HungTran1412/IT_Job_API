package backend.main.services.Impl;

import backend.main.utils.JwtUtils;
import backend.main.dto.request.ChangePasswordRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.entities.Admin;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.AdminRepository;
import backend.main.services.AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    @Transactional
    public String login(LoginRequest request) {
        // Tìm ứng viên theo email, nếu không có thì ném lỗi
        Admin c = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(Code.EMAIL_DOES_NOT_EXIST));

        // So sánh mật khẩu nhập vào với mật khẩu đã mã hóa trong DB
        if (!passwordEncoder.matches(request.getPassword(), c.getPassword())) {
            throw new AppException(Code.WRONG_PASSWORD);
        }

        // Trả về thông tin ứng viên (không bao gồm mật khẩu)
        return jwtUtils.generateToken(String.valueOf(c.getId()), c.getEmail(), c.getRole(), request.isRememberMe());
    }

    @Transactional
    @Override
    public boolean changePassword(String email, ChangePasswordRequest request) {
        try {
            Admin c = adminRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(Code.EMAIL_DOES_NOT_EXIST));

            if(c.getPassword() == null){
                throw new AppException(Code.PASSWORD_IS_NULL);
            }

            //kiểm tra mật khẩu cũ trước khi đổi
            if(!passwordEncoder.matches(request.getOldPassword(), c.getPassword())){
                throw new AppException(Code.OLD_PASSWORD_NOT_MATCH);
            }

            c.setPassword(passwordEncoder.encode(request.getNewPassword()));
            adminRepository.save(c);
            return true;
        } catch (AppException e) {
            return false;
        }
    }
}
