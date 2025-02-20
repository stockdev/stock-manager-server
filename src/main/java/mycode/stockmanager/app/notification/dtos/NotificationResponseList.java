package mycode.stockmanager.app.notification.dtos;

import java.util.List;

public record NotificationResponseList(
        List<NotificationResponse> list,
        int currentPage,
        int totalPages,
        long totalElements
) {
}
