package mycode.stockmanager.app.utilaje.dtos;


import java.util.List;

public record UtilajeResponseList(
        List<UtilajResponseDto> list,
        int currentPage,
        int totalPages,
        long totalElements
) {
}

