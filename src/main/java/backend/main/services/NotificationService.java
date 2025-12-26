package backend.main.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import backend.main.entities.Notification;

public interface NotificationService {
    void sendNotification(String receiverId, String content, String type);
    Page<Notification> getNotiByUser(String userId, Pageable pageable);
}
