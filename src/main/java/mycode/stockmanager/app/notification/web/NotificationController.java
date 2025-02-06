package mycode.stockmanager.app.notification.web;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.notification.dtos.NotificationResponseList;
import mycode.stockmanager.app.notification.services.NotificationCommandService;
import mycode.stockmanager.app.notification.services.NotificationQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-manager/api/notification")
@AllArgsConstructor
public class NotificationController {

    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    @GetMapping("/getAllNotifications")
    public ResponseEntity<NotificationResponseList> getAllNotifications() {
        NotificationResponseList response = notificationQueryService.getAllNotifications();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllNotifications")
    public ResponseEntity<String> deleteAllNotifications() {
        String result = notificationCommandService.deleteAllNotifications();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
