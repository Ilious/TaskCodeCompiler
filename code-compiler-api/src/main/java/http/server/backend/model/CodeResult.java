package http.server.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CodeResult {

    @JsonProperty("stdout")
    private final String codeResult;

    @JsonProperty("stderr")
    private final String codeError;
}
