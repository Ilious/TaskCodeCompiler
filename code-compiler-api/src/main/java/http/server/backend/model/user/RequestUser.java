package http.server.backend.model.user;

import jakarta.validation.constraints.NotBlank;

public record RequestUser(
        Long id,
        @NotBlank String login,
        @NotBlank String password) {
}
