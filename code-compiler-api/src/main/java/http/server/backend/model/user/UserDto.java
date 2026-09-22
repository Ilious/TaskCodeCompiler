package http.server.backend.model.user;

public record UserDto(
        Long id,

        String login,

        String password) {
}
