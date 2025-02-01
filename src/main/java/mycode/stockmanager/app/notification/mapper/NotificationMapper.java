package mycode.stockmanager.app.notification.mapper;

import mycode.stockmanager.app.notification.dtos.NotificationResponse;
import mycode.stockmanager.app.notification.model.Notification;

public class NotificationMapper {

    public static NotificationResponse notificationToResponseDto(Notification notification){

        return NotificationResponse.builder()
                .message(notification.getMessage())
                .notificationType(notification.getNotificationType())
                .user(notification.getUser()).build();

    }
}
