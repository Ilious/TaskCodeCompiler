package http.server.backend.mappers;

import http.server.backend.model.user.RequestUser;
import http.server.backend.model.user.ResponseUser;
import http.server.backend.model.user.User;
import http.server.backend.utils.LoginUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(source = "password", qualifiedByName = "encodePassword", target = "password")
    User toEntity(RequestUser dto);

    ResponseUser toDto(User user);

    @Named("encodePassword")
    default String encodePassword(String password) {
        return LoginUtils.encodePassword(password);
    }
}
