package backend.main.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.dto.request.NotificationMessageRequest;
import backend.main.dto.request.noti.ReadNotiRequest;
import backend.main.entities.Notification;
import backend.main.repository.NotificationRepository;
import backend.main.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public void sendNotification(String receiverId, String content, String sender) {
        NotificationMessageRequest notificationMessage = new NotificationMessageRequest(receiverId, sender, content);

        if ("admins".equals(receiverId)) {
            // Gửi thông báo chung cho tất cả admin
            messagingTemplate.convertAndSend("/topic/admins", notificationMessage);
        } else {
            // Gửi thông báo riêng cho người dùng cụ thể
            messagingTemplate.convertAndSendToUser(receiverId, "/queue/notifications", notificationMessage);
        }
        // Bạn có thể thêm logic để lưu notification vào DB ở đây nếu cần
        // notificationRepository.save(...)
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
			notifications.forEach(t -> t.setIsRead(notiRequest.isRead()));
			notificationRepository.saveAll(notifications);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
