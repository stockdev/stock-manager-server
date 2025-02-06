package mycode.stockmanager.app.location.dtos;

import java.util.List;

public record LocationResponseList(
        List<LocationResponse> list,
        int currentPage,
        int totalPages,
        long totalElements) {
}
