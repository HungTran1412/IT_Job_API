package backend.main.enums;

public enum NotificationType {

    JOB_APPROVED("Bài tuyển dụng đã được duyệt"),
    JOB_REJECTED("Bài tuyển dụng bị từ chối"),
    JOB_CANCELLED("Bài tuyển dụng đã bị hủy"),
    JOB_PENDING("Bài tuyển dụng đang chờ duyệt"),

    NEW_APPLICATION("Có ứng viên mới ứng tuyển"),
    APPLICATION_APPROVED("Hồ sơ ứng tuyển được chấp nhận"),
    APPLICATION_REJECTED("Hồ sơ ứng tuyển bị từ chối"),
    APPLICATION_REVIEW("Hồ sơ ứng tuyển đang được nhà tuyển dụng đánh giá"),

    SYSTEM("Thông báo hệ thống");

    private final String message;

    NotificationType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
