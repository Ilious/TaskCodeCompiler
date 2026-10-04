package http.server.dto;


import http.server.dto.enums.Compiler;
import http.server.dto.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskDto {

    private UUID id;

    private String code;

    private Compiler compiler;

    private Status status;
}
