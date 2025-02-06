package mycode.stockmanager.app.notification.services;

import mycode.stockmanager.app.notification.dtos.NotificationResponseList;

public interface NotificationQueryService {

    NotificationResponseList getAllNotifications();
}
