package mycode.stockmanager.app.magazie.dtos;


import lombok.Builder;

@Builder
public record MagazieResponse(String articleCode, String locationCode, int stock, int totalStock) {
}
