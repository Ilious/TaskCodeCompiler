package http.server.backend.service.interfaces;

import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.model.user.RequestUser;
import http.server.backend.model.user.ResponseUser;
import http.server.backend.model.user.User;

public interface IUserService {

    boolean existsUserByLogin(String login);

    ResponseUser loginUser(RequestUser user);

    ResponseUser createUser(RequestUser user) throws EntityExistsException;

    User getUserById(Long id);

    User getUserByLogin(String login);
}
