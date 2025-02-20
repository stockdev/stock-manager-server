package mycode.notesapp.app.users.dtos;

import jakarta.validation.constraints.NotNull;
import mycode.notesapp.app.system.security.UserRole;

public record CreateUserRequest(@NotNull String fullName,
                                @NotNull String email,
                                @NotNull String password,
                                @NotNull String phone,
                                @NotNull UserRole userRole) {
}
