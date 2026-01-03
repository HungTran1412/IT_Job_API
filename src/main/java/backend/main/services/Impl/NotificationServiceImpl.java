package backend.main.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.controller.LogoutContorller;
import backend.main.dto.request.noti.ReadNotiRequest;
import backend.main.entities.Notification;
import backend.main.enums.NotificationType;
import backend.main.enums.Role;
import backend.main.repository.NotificationRepository;
import backend.main.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final LogoutContorller logoutContorller;
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    NotificationServiceImpl(LogoutContorller logoutContorller) {
        this.logoutContorller = logoutContorller;
    }


	@Override
	public Page<Notification> getNotiByUser(String userId,Pageable pageable) {
		return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
	}

	@Override
	@Transactional
	public boolean readNoti(ReadNotiRequest notiRequest) {
		try {
			List<Notification> notifications = notificationRepository.findAllById(notiRequest.getNotiIds());
			notifications.forEach(t -> t.setIsRead(true));
			notificationRepository.saveAll(notifications);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}


	@Override
	@Transactional
	public Long saveNotification(String email, Role role, NotificationType type, String content, String from) {
		Notification notification = Notification.builder()
                .content(content)
                .isRead(false)
                .userId(email)
                .role(Role.ROLE_CANDIDATE)
                .type(type)
                .from(from)
                .build();
        notificationRepository.save(notification);
        return notification.getNotiId();
	}
}
