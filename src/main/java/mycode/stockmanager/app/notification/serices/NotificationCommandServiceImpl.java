package mycode.stockmanager.app.notification.serices;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService{

    private NotificationRepository notificationRepository;
}
