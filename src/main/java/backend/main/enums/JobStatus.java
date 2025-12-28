package backend.main.enums;

public enum JobStatus {

    APPROVED("Việc đã được duyệt"),
    REJECTED("Việc đã bị từ chối"),
    CANCELLED("Việc đã bị hủy"),
    PENDING("Việc đang chờ xét duyệt");

    private final String message;

    JobStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
