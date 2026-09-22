package http.server.backend.model.user;

import jakarta.validation.constraints.NotBlank;

public record RequestUser(
        @NotBlank String login,
        @NotBlank String password) {
}
