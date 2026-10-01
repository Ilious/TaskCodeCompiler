package http.server.queue.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import http.server.queue.model.enums.Status;
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

    private final Status status;
}
