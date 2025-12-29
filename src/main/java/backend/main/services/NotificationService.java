package backend.main.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import backend.main.dto.request.noti.ReadNotiRequest;
import backend.main.entities.Notification;

public interface NotificationService {
    Page<Notification> getNotiByUser(String userId, Pageable pageable);
    boolean readNoti(ReadNotiRequest notiRequest);
}
