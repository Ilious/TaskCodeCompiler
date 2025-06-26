package http.server.backend.model.queue;

import http.server.backend.model.CodeResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultMessage {

    private final CodeResult result;

    private final String correlationId;
}
