package http.server.backend.model.request;

import jakarta.validation.constraints.NotBlank;

public record RequestUser(
        @NotBlank String login,
        @NotBlank String password) {
}
