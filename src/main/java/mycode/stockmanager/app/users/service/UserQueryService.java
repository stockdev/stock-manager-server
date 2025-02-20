package mycode.stockmanager.app.users.service;

import mycode.stockmanager.app.users.dtos.UserResponse;
import mycode.stockmanager.app.users.dtos.UserResponseList;
import mycode.stockmanager.app.users.model.User;

public interface UserQueryService {

    UserResponse findUserById(long id);

    UserResponseList getAllUsers();

    User findByEmail(String email);

}
