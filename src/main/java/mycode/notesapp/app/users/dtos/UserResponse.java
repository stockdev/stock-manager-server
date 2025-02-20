package mycode.notesapp.app.users.dtos;

import mycode.notesapp.app.system.security.UserRole;

public record UserResponse(long id, String email, String password, String fullName, String phone, UserRole userRole) {
}
