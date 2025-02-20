package mycode.stockmanager.app.users.dtos;


import mycode.stockmanager.app.system.security.UserRole;

public record RegisterResponse(String jwtToken,
                               String fullName,
                               String phone,
                               String email,
                               UserRole userRole) {
}
