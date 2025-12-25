package backend.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.main.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByNotiIdOrderByCreatedAtDesc(Long notiId);
}
