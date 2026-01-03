package backend.main.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import backend.main.dto.request.noti.ReadNotiRequest;
import backend.main.entities.Notification;
import backend.main.enums.NotificationType;
import backend.main.enums.Role;

public interface NotificationService {
    Page<Notification> getNotiByUser(String userId, Pageable pageable);
    boolean readNoti(ReadNotiRequest notiRequest);
    Long saveNotification(String userId, Role role, NotificationType type, String content, String from);
}
