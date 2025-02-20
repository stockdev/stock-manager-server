package mycode.stockmanager.app.notification.services;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.notification.dtos.NotificationResponse;
import mycode.stockmanager.app.notification.dtos.NotificationResponseList;
import mycode.stockmanager.app.notification.enums.NotificationType;
import mycode.stockmanager.app.notification.exceptions.NotFoundNotification;
import mycode.stockmanager.app.notification.mapper.NotificationMapper;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private NotificationRepository notificationRepository;


    public NotificationResponseList getAllNotifications(int page, int size, String searchTerm, String selectedType) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());


        Specification<Notification> spec = Specification.where(null);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("message"), "%" + searchTerm + "%")
            );
        }

        if (selectedType != null && !selectedType.isEmpty()) {
            NotificationType type = NotificationType.valueOf(selectedType);
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("notificationType"), type)
            );
        }

        Page<Notification> notificationPage = notificationRepository.findAll(spec, pageable);

        if (notificationPage.isEmpty()) {
            throw new NotFoundNotification("No notifications found");
        }

        List<NotificationResponse> responses = notificationPage.getContent()
                .stream()
                .map(NotificationMapper::notificationToResponseDto)
                .collect(Collectors.toList());

        return new NotificationResponseList(
                responses,
                notificationPage.getNumber(),
                notificationPage.getTotalPages(),
                notificationPage.getTotalElements()
        );
    }


}


