package mycode.stockmanager.app.notification.mapper;

import mycode.stockmanager.app.notification.dtos.NotificationResponse;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.users.mapper.UserMapper;

public class NotificationMapper {

    public static NotificationResponse notificationToResponseDto(Notification notification){

        return NotificationResponse.builder()
                .message(notification.getMessage())
                .notificationType(notification.getNotificationType())
                .user(UserMapper.userToResponseDto(notification.getUser())).build();

    }
}
