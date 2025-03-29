package http.server.backend.controllers;

import http.server.backend.model.Session;
import http.server.backend.model.User;
import http.server.backend.model.request.RequestUser;
import http.server.backend.service.interfaces.ISessionService;
import http.server.backend.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(summary = "Register user by login and password", description = "register user in storage")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Success",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Fields null",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "User exists",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<User> registerUser(@RequestBody RequestUser user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user by login and password", description = "returns sessionId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Fields null",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Password is not correct",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> loginUser(@RequestBody RequestUser user) {
        User loggedUser = userService.loginUser(user);
        Session session = sessionService.createSession(loggedUser);

        return ResponseEntity.status(HttpStatus.OK).body(session.getId());
    }
}
