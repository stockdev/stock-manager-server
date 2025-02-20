package mycode.stockmanager.app.users.dtos;


import mycode.stockmanager.app.system.security.UserRole;

public record LoginResponse(String jwtToken,
                            Long id,
                            String fullName,
                            String phone,
                            String email,
                            UserRole userRole) {
}
