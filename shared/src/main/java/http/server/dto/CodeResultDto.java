package http.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import http.server.dto.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeResultDto {

    @JsonProperty("stdout")
    private String codeResult;

    @JsonProperty("stderr")
    private String codeError;

    private Status status;

    private Long exitCode;
}
