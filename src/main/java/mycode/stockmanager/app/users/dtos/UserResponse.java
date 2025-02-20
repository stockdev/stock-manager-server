package mycode.stockmanager.app.users.dtos;

import mycode.stockmanager.app.system.security.UserRole;

public record UserResponse(long id, String email, String password, String fullName, String phone, UserRole userRole) {
}
