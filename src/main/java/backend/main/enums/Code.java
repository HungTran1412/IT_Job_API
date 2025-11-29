package backend.main.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Code {
    UNCATEGORIZED_EXCEPTION(CodeEnum.error.toString(), "Lỗi chưa xác thực"),
    EMAIL_EXISTED(CodeEnum.error.toString(),"Email đã tồn tại!"),
    WRONG_PASSWORD(CodeEnum.error.toString(),"Sai mật khẩu!"),
    EMAIL_DOES_NOT_EXIST(CodeEnum.error.toString(),"Email không tồn tại!"),
    EMPLOYER_NOT_FOUND(CodeEnum.error.toString(), "Không tìm thấy nhà tuyển dụng!"),
    CANDIDATE_NOT_FOUND(CodeEnum.error.toString(), "Không tìm thấy ứng viên!"),
    TOKEN_INVALID(CodeEnum.error.toString(), "Token không hợp lệ!"),
    ACCOUNT_UNENABLED(CodeEnum.error.toString(), "Tài khoản chưa xác thực!"),
    ID_EXISTED(CodeEnum.error.toString(), "ID đã ồn tại!"),
    FILE_UPLOAD_FAILED(CodeEnum.error.toString(), "File Upload thất bại!"),
    LOGIN_SUCCEEDED(CodeEnum.success.toString(), "Đăng nhập thành công!"),
    UPDATE_INFO_SUCCEEDED(CodeEnum.success.toString(), "Cập nhật thông tin thành công!"),
    UPDATE_INFO_FAILED(CodeEnum.error.toString(), "Cập nhật thông tin thất bại!"),
    CANNOT_UPDATE_ANOTHER_USER(CodeEnum.error.toString(), "Không thể cập nhật thông tin của người khác!"),
    CANNOT_GET_ANOTHER_INFO(CodeEnum.error.toString(), "Cannot get another info!"),
    GET_INFO_SUCCEEDED(CodeEnum.success.toString(), "Lấy thông tin thành công!"),
    TOKEN_EXPIRED(CodeEnum.error.toString(), "Token hết hạn. Vui lòng đăng nhập lại!"),
    USER_NOT_FOUND(CodeEnum.error.toString(),"Người dùng không tồn tại"),
    TOKEN_VALID(CodeEnum.success.toString(), "Token hợp lệ"),
    INTERNAL_ERROR(CodeEnum.error.toString(), ""),
    TOKEN_NULL(CodeEnum.error.toString(), "Token rỗng"),
    SERVER_ERROR(CodeEnum.error.toString(), "Lỗi không mong muốn khi kiểm tra mã token"),
    PASSWORD_IS_NULL(CodeEnum.error.toString(),"Mật khẩu trống"),
    OLD_PASSWORD_NOT_MATCH(CodeEnum.error.toString(), "Mật khẩu cũ không đúng!"),
    PASSWORD_CHANGED(CodeEnum.success.toString(),"Đã đổi mật khẩu"),
    ACCOUNT_VERIFIED(CodeEnum.success.toString(),"Tài khoản đã xác thực"),
    RESEND_COMPLETE(CodeEnum.success.toString(), "Gửi lại email thành công"),
    REGISTER_SUCCESSED(CodeEnum.success.toString(), "Đăng ký thành công, vui lòng check email!"),
    LOGOUT_SUCCESSED(CodeEnum.success.toString(), "Đăng xuất thành công"),
    JOB_NOT_FOUND(CodeEnum.error.toString(), "Không tìm thấy công việc"),
    JOB_APROVED(CodeEnum.success.toString(), "Đã duyệt việc làm"),
    CREATE_JOB_SUCCESSFULL(CodeEnum.success.toString(), "Tạo việc làm thành công"),
    UPDATE_JOB_SUCCESSFULL(CodeEnum.success.toString(), "Cập nhật việc làm thành công"),
    GET_JOB_SUCCESSFULL(CodeEnum.success.toString(), "Lấy việc làm thành công"),

    EMAIL_INVALID_FORMAT(CodeEnum.error.toString(), "Email không đúng định dạng"),
    PASSWORD_INVALID_FORMAT(CodeEnum.error.toString(), "Mật khẩu không hợp lệ"),

    // Mã mới cho chức năng quên mật khẩu
    FORGOT_PASSWORD_SUCCESS(CodeEnum.success.toString(), "Nếu email của bạn tồn tại trong hệ thống, chúng tôi đã gửi một mã OTP."),
    RESET_PASSWORD_SUCCESS(CodeEnum.success.toString(), "Mật khẩu của bạn đã được đặt lại thành công."),
    LIKED_JOB_ADDED(CodeEnum.error.toString(), "Thêm việc yêu thích thành công"),
    DELETED_SUCCESSFULLY(CodeEnum.success.toString(),"Xóa thành công"),
    INVALID_VALUE(CodeEnum.error.toString(),"Lỗi dữ liệu")
    
    ;

    String code;
    String message;

    public enum CodeEnum {
        success,
        error,
    }
}
