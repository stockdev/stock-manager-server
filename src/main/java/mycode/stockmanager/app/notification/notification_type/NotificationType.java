package mycode.stockmanager.app.notification.notification_type;



import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public enum NotificationType {

    INFORMATION(Set.of()),
    WARNING(Set.of()),
    ERROR(Set.of());
    private final Set<NotificationType> notificationTypes;

}
