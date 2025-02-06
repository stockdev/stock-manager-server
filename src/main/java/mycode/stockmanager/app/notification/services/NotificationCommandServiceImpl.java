package mycode.stockmanager.app.notification.services;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService{

    private final NotificationRepository notificationRepository;

    @Override
    public String deleteAllNotifications() {
        notificationRepository.deleteAll();
        return "All notifications deleted";
    }
}
