package http.server.backend.service.interfaces;

import http.server.backend.model.User;
import http.server.backend.model.request.RequestUser;
import http.server.backend.exceptions.storage.EntityExistsException;

public interface IUserService {

    boolean existsUserByLogin(String login);

    User loginUser(RequestUser user);

    User createUser(RequestUser user) throws EntityExistsException;
}
