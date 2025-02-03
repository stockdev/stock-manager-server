package mycode.stockmanager.app.users.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mycode.stockmanager.app.system.jwt.JWTTokenProvider;
import mycode.stockmanager.app.users.dtos.*;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.service.UserCommandService;
import mycode.stockmanager.app.users.service.UserQueryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static mycode.stockmanager.app.system.constants.Constants.JWT_TOKEN_HEADER;

@RestController
@AllArgsConstructor
@RequestMapping("/stock-manager/api/user")
@CrossOrigin
@Slf4j
public class UserController {
    
    private UserCommandService userCommandService;
    private UserQueryService userQueryService;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;



    @GetMapping("/getAllUsers")
    public ResponseEntity<UserResponseList> getAllUsers(){
        return new ResponseEntity<>(userQueryService.getAllUsers(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_UTILIZATOR')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        String responseMessage = userCommandService.deleteUser(email);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_UTILIZATOR')")
    @PutMapping("/update/{email}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable String email) {
        return new ResponseEntity<>(userCommandService.updateUser(updateUserRequest, email), HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user) {

        User loginUser = userQueryService.findByEmail(user.email());
        User userPrincipal = getUser(loginUser);

        authenticate(user.email(), user.password());
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        LoginResponse loginResponse = new LoginResponse(
                jwtHeader.getFirst(JWT_TOKEN_HEADER),
                userPrincipal.getId(),
                userPrincipal.getFullName(),
                userPrincipal.getPhone(),
                userPrincipal.getEmail(),
                userPrincipal.getUserRole()
        );
        return new ResponseEntity<>(loginResponse, jwtHeader, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody CreateUserRequest createUserRequest){
        this.userCommandService.createUser(createUserRequest);
        User userPrincipal = userQueryService.findByEmail(createUserRequest.email());
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        RegisterResponse registerResponse = new RegisterResponse(
                jwtHeader.getFirst(JWT_TOKEN_HEADER),
                userPrincipal.getFullName(),
                userPrincipal.getPhone(),
                userPrincipal.getEmail(),
                userPrincipal.getUserRole()
        );
        return new ResponseEntity<>(registerResponse, jwtHeader, HttpStatus.CREATED);


    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJWTToken(user));
        return headers;
    }

    private User getUser(User loginUser) {
        User userPrincipal = new User();
        userPrincipal.setEmail(loginUser.getEmail());
        userPrincipal.setId(loginUser.getId());
        userPrincipal.setPassword(loginUser.getPassword());
        userPrincipal.setUserRole(loginUser.getUserRole());
        userPrincipal.setFullName(loginUser.getFullName());
        userPrincipal.setPhone(loginUser.getPhone());
        return userPrincipal;
    }
}
