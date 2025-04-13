package http.server.backend.model.request;

import jakarta.validation.constraints.NotBlank;

public record RequestTask(
        @NotBlank String code,
        @NotBlank String compiler) {
}
