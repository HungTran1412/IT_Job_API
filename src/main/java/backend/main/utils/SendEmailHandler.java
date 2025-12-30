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
}
