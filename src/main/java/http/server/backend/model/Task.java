package http.server.backend.model;


import http.server.backend.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Task {

    private String id;

    private String code;

    private String compiler;

    private Status status;
}
