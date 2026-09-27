package http.server.backend.mappers;

import http.server.backend.model.session.Session;
import http.server.backend.model.session.SessionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SessionMapper {

    @Mapping(target = "userId", source = "user.id")
    SessionDto toDto(Session session);
}
