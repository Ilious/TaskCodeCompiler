package http.server.backend.mappers;

import http.server.backend.model.codeResult.CodeResult;
import http.server.dto.CodeResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CodeResultMapper {

    CodeResultDto toDto(CodeResult codeResult);
}
