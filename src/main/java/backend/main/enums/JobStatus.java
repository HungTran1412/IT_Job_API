package backend.main.enums;

public enum JobStatus {

    APPROVED("Việc %s đã được duyệt"),
    REJECTED("Việc %s đã bị từ chối"),
    CANCELLED("Việc %s đã bị hủy"),
    PENDING("Việc %s đang chờ xét duyệt");

    private final String message;

    JobStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
