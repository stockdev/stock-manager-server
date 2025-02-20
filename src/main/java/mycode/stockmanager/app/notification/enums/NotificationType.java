package mycode.stockmanager.app.notification.enums;



import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public enum NotificationType {

    INFORMATION(Set.of()),
    WARNING(Set.of()),
    ERROR(Set.of());
    private final Set<NotificationType> notificationTypes;

}
