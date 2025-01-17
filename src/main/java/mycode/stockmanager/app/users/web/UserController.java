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

    @GetMapping(path = "/getUserById/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId){
        return new ResponseEntity<>(userQueryService.findUserById(userId), HttpStatus.OK);
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserResponse> addUser(@RequestBody CreateUserRequest createUserRequest){
        return new ResponseEntity<>(userCommandService.createUser(createUserRequest), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/deleteUserById/{userId}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable long userId){
        return new ResponseEntity<>(userCommandService.deleteUser(userId), HttpStatus.CREATED);
    }

    @PutMapping(path = "/updateUserById/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable long userId, @RequestBody UpdateUserRequest updateUserRequest){
    return new ResponseEntity<>(userCommandService.updateUser(updateUserRequest, userId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<UserResponseList> getAllUsers(){
        return new ResponseEntity<>(userQueryService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/getUserRole")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_HELPER')")
    public ResponseEntity<String> getUserRole(@RequestHeader("Authorization") String token) {
        try {
            String tokenValue = extractToken(token);
            String username = jwtTokenProvider.getSubject(tokenValue);
            if (jwtTokenProvider.isTokenValid(username, tokenValue)) {
                User loginUser = userQueryService.findByEmail(username);
                return ResponseEntity.ok(loginUser.getUserRole().toString());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while verifying token");
        }
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
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
