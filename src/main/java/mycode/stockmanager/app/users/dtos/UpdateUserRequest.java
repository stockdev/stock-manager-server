package mycode.stockmanager.app.users.dtos;

import jakarta.validation.constraints.NotNull;
import mycode.stockmanager.app.system.security.UserRole;

public record UpdateUserRequest(@NotNull String fullName,
                                @NotNull String email,
                                @NotNull String password,
                                @NotNull String phone ,

                                @NotNull UserRole userRole) {
}
