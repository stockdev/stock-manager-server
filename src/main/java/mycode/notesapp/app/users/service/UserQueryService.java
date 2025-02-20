package mycode.notesapp.app.users.service;

import mycode.notesapp.app.users.dtos.UserResponse;
import mycode.notesapp.app.users.dtos.UserResponseList;
import mycode.notesapp.app.users.model.User;

public interface UserQueryService {

    UserResponse findUserById(long id);

    UserResponseList getAllUsers();

    User findByEmail(String email);

}
