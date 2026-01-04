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
        <div style="font-family: Arial, sans-serif; color: #333; border: 1px solid #ddd; padding: 20px; max-width: 600px; margin: auto; border-radius: 8px;">
            <h2 style="color: #0056b3;">Chào mừng bạn đến với ITJob!</h2>
            <p>Cảm ơn bạn đã đăng ký tài khoản.</p>
            <p>Vui lòng xác nhận tài khoản của bạn bằng cách nhấn vào nút bên dưới:</p>

            <p style="text-align: center; margin: 30px 0;">
                <a href="%s"
                   style="display:inline-block;background-color:#28a745;color:white;
                          padding:12px 24px;text-decoration:none;border-radius:5px;
                          font-weight:bold; font-size: 16px;">
                    Xác nhận tài khoản
                </a>
            </p>
                
            <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin-top: 20px;">
                <p style="margin: 0; font-size: 14px; color: #555;">Nếu nút trên không hoạt động, vui lòng sao chép và dán liên kết sau vào trình duyệt:</p>
                <p style="margin-top: 5px; word-break: break-all;">
                    <a href="%s" style="color:#0056b3;">%s</a>
                </p>
            </div>

            <p style="margin-top: 20px; font-size: 14px; color: #777;">Liên kết này sẽ hết hạn sau 5 phút.</p>
            <p style="font-size: 14px; color: #777;">Nếu bạn không thực hiện đăng ký này, vui lòng bỏ qua email này.</p>
            <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
            <p style="font-size: 12px; color: #777; text-align: center;">&copy; 2024 ITJob. All rights reserved.</p>
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

    public void sendApplicationNotification(String email, String jobTitle, String companyName, String candidateName, boolean isToEmployer, String cvUrl) {
        String subject;
        String content;

        if (isToEmployer) {
            subject = "Ứng viên mới cho công việc: " + jobTitle;
            content = """
                <div style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                    <h2 style="color: #0056b3;">Thông báo ứng tuyển mới</h2>
                    <p>Xin chào <strong>%s</strong>,</p>
                    <p>Bạn vừa nhận được một hồ sơ ứng tuyển mới cho vị trí <strong>%s</strong> từ ứng viên <strong>%s</strong>.</p>
                    <p>Bạn có thể xem CV của ứng viên tại đường dẫn sau:</p>
                    <p style="text-align: center; margin: 20px 0;">
                        <a href="%s" style="display:inline-block;background-color:#007bff;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;font-weight:bold;">Xem CV</a>
                    </p>
                    <p>Hoặc đăng nhập vào hệ thống để xem chi tiết hồ sơ.</p>
                    <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                    <p style="font-size: 12px; color: #777; text-align: center;">&copy; 2024 ITJob. All rights reserved.</p>
                </div>
                """.formatted(companyName, jobTitle, candidateName, cvUrl);
        } else {
            subject = "Ứng tuyển thành công: " + jobTitle;
            content = """
                <div style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                    <h2 style="color: #28a745;">Ứng tuyển thành công!</h2>
                    <p>Xin chào <strong>%s</strong>,</p>
                    <p>Hồ sơ của bạn cho vị trí <strong>%s</strong> tại công ty <strong>%s</strong> đã được gửi thành công.</p>
                    <p>Nhà tuyển dụng sẽ xem xét hồ sơ và phản hồi lại bạn trong thời gian sớm nhất.</p>
                    <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                    <p style="font-size: 12px; color: #777; text-align: center;">&copy; 2024 ITJob. All rights reserved.</p>
                </div>
                """.formatted(candidateName, jobTitle, companyName);
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send application notification email to {}", email, e);
        }
    }
    
    public void sendApplicationStatusNotification(String email, String jobTitle, String companyName, String candidateName, String status) {
        String subject = "Cập nhật trạng thái ứng tuyển: " + jobTitle;
        String statusMessage = "";
        String color = "#333";

        if ("REVIEWING".equals(status)) {
            statusMessage = "Hồ sơ của bạn đã được nhà tuyển dụng xem.";
            color = "#17a2b8"; // Info color
        } else if ("REJECTED".equals(status)) {
            statusMessage = "Rất tiếc, nhà tuyển dụng đã từ chối hồ sơ của bạn.";
            color = "#dc3545"; // Danger color
        } else {
            return; // Không gửi email cho các trạng thái khác nếu không cần thiết
        }

        String content = """
            <div style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                <h2 style="color: %s;">Cập nhật trạng thái hồ sơ</h2>
                <p>Xin chào <strong>%s</strong>,</p>
                <p>Trạng thái hồ sơ ứng tuyển của bạn cho vị trí <strong>%s</strong> tại công ty <strong>%s</strong> đã thay đổi:</p>
                <p style="font-size: 16px; font-weight: bold; color: %s;">%s</p>
                <p>Vui lòng đăng nhập vào hệ thống để biết thêm chi tiết.</p>
                <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                <p style="font-size: 12px; color: #777; text-align: center;">&copy; 2024 ITJob. All rights reserved.</p>
            </div>
            """.formatted(color, candidateName, jobTitle, companyName, color, statusMessage);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send application status notification email to {}", email, e);
        }
    }

    public void sendJobReviewNotification(String email, String jobTitle, String companyName, String status) {
        String subject = "Thông báo duyệt tin tuyển dụng: " + jobTitle;
        String statusMessage = "";
        String color = "#333";

        if ("APPROVED".equals(status)) {
            statusMessage = "Tin tuyển dụng của bạn đã được duyệt và hiển thị trên hệ thống.";
            color = "#28a745"; // Success color
        } else if ("REJECTED".equals(status)) {
            statusMessage = "Tin tuyển dụng của bạn đã bị từ chối. Vui lòng kiểm tra lại nội dung.";
            color = "#dc3545"; // Danger color
        } else {
            return;
        }

        String content = """
            <div style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                <h2 style="color: %s;">Thông báo duyệt tin</h2>
                <p>Xin chào <strong>%s</strong>,</p>
                <p>Trạng thái tin tuyển dụng <strong>%s</strong> của bạn đã được cập nhật:</p>
                <p style="font-size: 16px; font-weight: bold; color: %s;">%s</p>
                <p>Vui lòng đăng nhập vào hệ thống để quản lý tin tuyển dụng.</p>
                <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                <p style="font-size: 12px; color: #777; text-align: center;">&copy; 2024 ITJob. All rights reserved.</p>
            </div>
            """.formatted(color, companyName, jobTitle, color, statusMessage);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send job review notification email to {}", email, e);
        }
    }
}
