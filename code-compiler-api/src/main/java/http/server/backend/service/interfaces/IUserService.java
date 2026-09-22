package http.server.backend.service.interfaces;

import http.server.backend.model.user.User;
import http.server.backend.model.user.UserDto;
import http.server.backend.model.user.RequestUser;
import http.server.backend.exceptions.storage.EntityExistsException;

public interface IUserService {

    boolean existsUserByLogin(String login);

    UserDto loginUser(RequestUser user);

    UserDto createUser(RequestUser user) throws EntityExistsException;

    User getUserById(Long id);
}
