package http.server.backend.model.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {

    private final int code;

    private final String description;
}
