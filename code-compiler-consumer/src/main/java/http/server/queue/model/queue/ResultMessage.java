package http.server.queue.model.queue;

import http.server.queue.model.CodeResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultMessage {

    private final CodeResult result;

    private final String correlationId;
}
