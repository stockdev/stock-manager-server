package mycode.stockmanager.app.notification.serices;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.notification.dtos.NotificationResponse;
import mycode.stockmanager.app.notification.dtos.NotificationResponseList;
import mycode.stockmanager.app.notification.mapper.NotificationMapper;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class NotificationQueryServiceImpl implements NotificationQueryService{

    private NotificationRepository notificationRepository;

    @Override
    public NotificationResponseList getAllNotifications() {
        List<Notification> list = notificationRepository.findAll();

        List<NotificationResponse> responses = new ArrayList<>();

        list.forEach(notification -> {
            responses.add(NotificationMapper.notificationToResponseDto(notification));
        });

        return new NotificationResponseList(responses);
    }
}


