package http.server.backend.repository.interfaces;

import http.server.backend.model.user.UserDto;
import http.server.backend.exceptions.storage.EntityNotFoundException;

public interface IUserRepo {

    UserDto postUser(UserDto userDto);

    UserDto getUserByLogin(String login) throws EntityNotFoundException;

    boolean userExists(String login);
}
