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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllNotifications")
    public ResponseEntity<String> deleteAllNotifications() {
        String result = notificationCommandService.deleteAllNotifications();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getAllNotifications")
    public ResponseEntity<NotificationResponseList> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String selectedType
    ) {
        NotificationResponseList response = notificationQueryService.getAllNotifications(page, size, searchTerm, selectedType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
