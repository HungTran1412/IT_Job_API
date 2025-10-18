package backend.main.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Service
public class SendEmailHandler {
    @Autowired
    JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String verifyLink) {
        String subject = "Xác nhận đăng ký tài khoản ITJob";

        String content = """
            <p>Xin chào,</p>
            <p>Bạn vừa đăng ký tài khoản tại hệ thống ITJob.</p>
            <p>Vui lòng xác nhận tài khoản của bạn bằng cách nhấn vào nút bên dưới:</p>
            <a href="%s" style="display:inline-block;background-color:#4CAF50;color:white;
                padding:10px 20px;text-decoration:none;border-radius:5px;">Xác nhận tài khoản</a>
            <p>Nếu bạn không thực hiện đăng ký này, vui lòng bỏ qua email này.</p>
            """.formatted(verifyLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendOTPEmail(String email, String otpLink) {}
}
