package http.server.queue.model;


import http.server.queue.model.enums.Status;
import http.server.queue.model.enums.Compiler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Task {

    private String id;

    private String code;

    private Compiler compiler;

    private Status status;
}
