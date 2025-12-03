package backend.main.services.Impl;

import backend.main.repository.NotificationRepository;
import backend.main.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public void sendNotification(String receiverId, String content, String type) {

    }
}
