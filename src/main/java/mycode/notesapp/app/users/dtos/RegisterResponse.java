package mycode.notesapp.app.users.dtos;


import mycode.notesapp.app.system.security.UserRole;

public record RegisterResponse(String jwtToken,
                               String fullName,
                               String phone,
                               String email,
                               UserRole userRole) {
}
