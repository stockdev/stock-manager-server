package mycode.stockmanager.app.users.service;

import mycode.stockmanager.app.users.dtos.CreateUserRequest;
import mycode.stockmanager.app.users.dtos.UpdateUserRequest;
import mycode.stockmanager.app.users.dtos.UserResponse;

public interface UserCommandService {

    void createUser(CreateUserRequest createUserRequest);

    String deleteUser(String email);

    UserResponse updateUser(UpdateUserRequest updateUserRequest, String email);

}
