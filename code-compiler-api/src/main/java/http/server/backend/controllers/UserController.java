package http.server.backend.controllers;

import http.server.backend.model.api.BearerToken;
import http.server.backend.model.session.SessionDto;
import http.server.backend.model.user.RequestUser;
import http.server.backend.model.user.ResponseUser;
import http.server.backend.service.interfaces.ISessionService;
import http.server.backend.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final IUserService userService;

    private final ISessionService sessionService;

    public UserController(IUserService userService, ISessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register a user",
            description = "Register a new user with a login and password"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "User with this login already exists")
    })
    public ResponseUser registerUser(@RequestBody @Valid RequestUser user) {
        return userService.createUser(user);
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Login",
            description = "Authenticate a user and create a new session. Returns a bearer token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Login successful and session created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Invalid login or password")
    })
    public BearerToken loginUser(@RequestBody @Valid RequestUser user) {
        ResponseUser loggedUserDto = userService.loginUser(user);
        SessionDto sessionDto = sessionService.createSession(loggedUserDto);

        return BearerToken.of(
                String.valueOf(sessionDto.getId())
        );
    }
}
