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
    TOKEN_NOT_VERIFIED(CodeEnum.error.toString(), "Token chưa được xác thực!"),
    TOKEN_NOT_FOUND_IN_COOKIE(CodeEnum.error.toString(), "Không tìm thấy token trong cookie. Vui lòng xác thực lại OTP."),
    ACCOUNT_UNENABLED(CodeEnum.error.toString(), "Tài khoản chưa xác thực!"),
    ACCOUNT_LOCKED(CodeEnum.error.toString(), "Tài khoản đã bị khóa!"),
    ID_EXISTED(CodeEnum.error.toString(), "ID đã ồn tại!"),
    FILE_UPLOAD_FAILED(CodeEnum.error.toString(), "File Upload thất bại!"),
    LOGIN_SUCCEEDED(CodeEnum.success.toString(), "Đăng nhập thành công!"),
    UPDATE_INFO_SUCCEEDED(CodeEnum.success.toString(), "Cập nhật thông tin thành công!"),
    UPDATE_LOCK_SUCCEEDED(CodeEnum.success.toString(),"Cập nhật trạng thái khóa thành công"),
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

    APPLY_SUCCESSFUL(CodeEnum.success.toString(),"Ứng tuyển thành công"),
    APPLY_FAIL(CodeEnum.error.toString(),"Ứng tuyển thất bại"),
    SEARCH_RESULT(CodeEnum.success.toString(),"Kết quả tìm kiếm"),
    GET_NOTI_SUCCESSFULL(CodeEnum.success.toString(),"Lấy thông báo thành công"),
    READ_NOTI(CodeEnum.success.toString(),"Đã đọc tin nhắn"),



    EMAIL_INVALID_FORMAT(CodeEnum.error.toString(), "Email không đúng định dạng"),
    PASSWORD_INVALID_FORMAT(CodeEnum.error.toString(), "Mật khẩu không hợp lệ"),

    // Mã mới cho chức năng quên mật khẩu
    FORGOT_PASSWORD_SUCCESS(CodeEnum.success.toString(), "Nếu email của bạn tồn tại trong hệ thống, chúng tôi đã gửi một mã OTP."),
    VERIFY_OTP_SUCCESS(CodeEnum.success.toString(), "Xác thực OTP thành công."),
    RESET_PASSWORD_SUCCESS(CodeEnum.success.toString(), "Mật khẩu của bạn đã được đặt lại thành công."),
    LIKED_JOB_ADDED(CodeEnum.error.toString(), "Thêm việc yêu thích thành công"),
    DELETED_SUCCESSFULLY(CodeEnum.success.toString(),"Xóa thành công"),
    DELETED_USER_SUCCESSFULLY(CodeEnum.success.toString(),"Xóa người dùng thành công"),

    INVALID_VALUE(CodeEnum.error.toString(),"Lỗi dữ liệu"),
    UNAUTHORIZED(CodeEnum.error.toString(),"Lỗi"),

    ACCESS_DENIED(CodeEnum.error.toString(), "Từ chối truy cập"),
    
    // Payment
    PAYMENT_SUCCESS(CodeEnum.success.toString(), "Thanh toán thành công"),
    PAYMENT_FAILED(CodeEnum.error.toString(), "Thanh toán thất bại"),
    VIP_PACKAGE_NOT_FOUND(CodeEnum.error.toString(), "Không tìm thấy gói VIP!"),
    ORDER_NOT_FOUND(CodeEnum.error.toString(), "Không tìm thấy đơn hàng!"),
    CREATE_PAYMENT_URL_SUCCESSFUL(CodeEnum.success.toString(), "Tạo URL thanh toán thành công"),
    
    // Vip Package
    CREATE_VIP_PACKAGE_SUCCESS(CodeEnum.success.toString(), "Tạo gói VIP thành công"),
    UPDATE_VIP_PACKAGE_SUCCESS(CodeEnum.success.toString(), "Cập nhật gói VIP thành công"),
    DELETE_VIP_PACKAGE_SUCCESS(CodeEnum.success.toString(), "Xóa gói VIP thành công"),
    GET_VIP_PACKAGE_SUCCESS(CodeEnum.success.toString(), "Lấy thông tin gói VIP thành công"),
    
    // Job Limit
    JOB_POST_WEEKLY_LIMIT_EXCEEDED(CodeEnum.error.toString(), "Bạn đã đạt giới hạn số lượng bài đăng trong tuần của gói hiện tại!"),
    JOB_POST_TOTAL_LIMIT_EXCEEDED(CodeEnum.error.toString(), "Bạn đã đạt giới hạn tổng số lượng bài đăng của gói hiện tại!"),
    
    // Subscription
    SUBSCRIPTION_NOT_FOUND(CodeEnum.error.toString(), "Không tìm thấy gói đăng ký!"),
    
    // Email
    SEND_EMAIL_FAILED(CodeEnum.error.toString(), "Gửi email thất bại!"),
    
    // Order
    CREATE_ORDER_SUCCESS(CodeEnum.success.toString(), "Tạo đơn hàng thành công"),
    GET_ORDER_SUCCESS(CodeEnum.success.toString(), "Lấy thông tin đơn hàng thành công"),
    UPDATE_ORDER_STATUS_SUCCESS(CodeEnum.success.toString(), "Cập nhật trạng thái đơn hàng thành công"),
    
    // Dashboard
    GET_DASHBOARD_STATS_SUCCESS(CodeEnum.success.toString(), "Lấy thống kê dashboard thành công"),
    
    // Date
    INVALID_DATE_RANGE(CodeEnum.error.toString(), "Ngày bắt đầu không được lớn hơn ngày kết thúc!")
    ;

    String code;
    String message;

    private enum CodeEnum {
        success,
        error,
    }
}
