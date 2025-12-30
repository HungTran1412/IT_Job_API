package backend.main.utils;

import backend.main.enums.Code;
import backend.main.exception.AppException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Service
public class SendEmailHandler {
    @Autowired
    JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String verifyLink) {
        String subject = "Xác nhận đăng ký tài khoản ITJob";

        String content = """
        <div style="font-family: Arial, sans-serif; color: #333;">
            <h2>Chào mừng bạn đến với ITJob!</h2>
            <p>Cảm ơn bạn đã đăng ký tài khoản.</p>
            <p>Vui lòng xác nhận tài khoản của bạn bằng cách nhấn vào nút bên dưới:</p>

            <p>
                        <a href="%s"
                           style="display:inline-block;background-color:#28a745;color:white;
                                  padding:12px 24px;text-decoration:none;border-radius:5px;
                                  font-weight:bold;">
                            Xác nhận tài khoản
                   </a>
             </p>
                

            <p>Nếu nút trên không hoạt động, vui lòng sao chép và dán liên kết sau vào trình duyệt:</p>
            <p>
                <a href="%s" style="color:#0056b3; word-break: break-all;">
                    %s
                </a>
            </p>

            <p>Liên kết này sẽ hết hạn sau 5 phút.</p>
            <p>Nếu bạn không thực hiện đăng ký này, vui lòng bỏ qua email này.</p>
        </div>
        """.formatted(verifyLink, verifyLink, verifyLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new AppException(Code.SEND_EMAIL_FAILED);
        }
    }


    public void sendOTPEmail(String email, String otp) {
        String subject = "Yêu cầu đặt lại mật khẩu - Mã OTP của bạn";

        String content = """
            <div style="font-family: Arial, sans-serif; color: #333; border: 1px solid #ddd; padding: 20px; max-width: 600px; margin: auto; border-radius: 8px;">
                <h2 style="color: #0056b3;">Yêu cầu đặt lại mật khẩu</h2>
                <p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>
                <p>Mã OTP của bạn là:</p>
                <p style="font-size: 24px; font-weight: bold; color: #d9534f; letter-spacing: 2px; text-align: center; padding: 10px; border: 1px dashed #ddd; background-color: #f9f9f9;">
                    %s
                </p>
                <p>Mã này sẽ hết hạn sau 5 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>
                <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
            </div>
            """.formatted(otp);
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new AppException(Code.SEND_EMAIL_FAILED);
        }
    }

    public void sendWelcomeEmail(String email, String name) {
        String subject = "Chào mừng bạn đến với ITJob - Đăng ký thành công!";

        String content = """
            <div style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                <h2 style="color: #28a745; text-align: center;">Đăng ký tài khoản thành công!</h2>
                <p>Xin chào <strong>%s</strong>,</p>
                <p>Chúc mừng bạn đã xác thực tài khoản thành công và chính thức trở thành thành viên của cộng đồng <strong>ITJob</strong>.</p>
                
                <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;">
                    <h3 style="color: #0056b3; margin-top: 0;">Bước tiếp theo: Hoàn thiện thông tin</h3>
                    <p>Để có trải nghiệm tốt nhất trên hệ thống, bạn hãy dành chút thời gian cập nhật đầy đủ thông tin hồ sơ cá nhân (đối với Ứng viên) hoặc thông tin doanh nghiệp (đối với Nhà tuyển dụng) tại trang quản lý nhé.</p>
                </div>

                <p>Nếu bạn cần hỗ trợ, đừng ngần ngại liên hệ với chúng tôi qua email này.</p>
                <p>Chúc bạn có những trải nghiệm tuyệt vời cùng ITJob!</p>
                <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                <p style="font-size: 12px; color: #777; text-align: center;">&copy; 2024 ITJob. All rights reserved.</p>
            </div>
            """.formatted(name);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            // Log lỗi nhưng không throw exception để tránh ảnh hưởng luồng chính (vì email chào mừng không quá quan trọng)
            log.error("Failed to send welcome email to {}", email, e);
        }
    }
}
