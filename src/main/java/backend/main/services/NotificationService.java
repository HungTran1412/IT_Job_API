package backend.main.services;

public interface NotificationService {
    void sendNotification(String receiverId, String content, String type);
}
