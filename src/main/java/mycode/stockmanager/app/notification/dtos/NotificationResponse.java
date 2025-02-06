package mycode.stockmanager.app.notification.dtos;

import lombok.Builder;
import mycode.stockmanager.app.notification.enums.NotificationType;
import mycode.stockmanager.app.stock.model.Stock;
import mycode.stockmanager.app.users.dtos.UserResponse;
import mycode.stockmanager.app.users.model.User;

@Builder
public record NotificationResponse(NotificationType notificationType, String message, UserResponse user, Stock stock) {
}
