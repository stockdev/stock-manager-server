package mycode.notesapp.app.users.service;

import mycode.notesapp.app.users.dtos.CreateUserRequest;
import mycode.notesapp.app.users.dtos.UpdateUserRequest;
import mycode.notesapp.app.users.dtos.UserResponse;

public interface UserCommandService {

    void createUser(CreateUserRequest createUserRequest);

    String deleteUser(String email);

    UserResponse updateUser(UpdateUserRequest updateUserRequest, String email);

}
