package http.server.backend.mappers;

import http.server.backend.model.session.Session;
import http.server.backend.model.session.SessionDto;
import http.server.backend.model.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SessionMapper {

    @Mapping(target = "duration", expression = "java(java.time.Duration.ofMinutes(10))")
    @Mapping(target = "stTime", expression = "java(java.time.Instant.now())")
    Session toEntity(UserDto dto);

    SessionDto toDto(Session session);
}
