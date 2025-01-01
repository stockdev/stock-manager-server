package mycode.stockmanager.app.users.service;

import mycode.stockmanager.app.users.dtos.CreateUserRequest;
import mycode.stockmanager.app.users.dtos.UpdateUserRequest;
import mycode.stockmanager.app.users.dtos.UserResponse;

public interface UserCommandService {

    UserResponse createUser(CreateUserRequest createUserRequest);

    UserResponse deleteUser(long id);

    UserResponse updateUser(UpdateUserRequest updateUserRequest, long id);

}
