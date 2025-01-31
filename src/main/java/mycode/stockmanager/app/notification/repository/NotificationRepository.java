package mycode.stockmanager.app.notification.repository;

import mycode.stockmanager.app.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
