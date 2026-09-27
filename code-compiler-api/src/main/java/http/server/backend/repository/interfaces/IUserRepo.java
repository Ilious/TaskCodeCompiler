package http.server.backend.repository.interfaces;

import http.server.backend.model.user.RequestUser;
import http.server.backend.model.user.ResponseUser;
import http.server.backend.exceptions.storage.EntityNotFoundException;

public interface IUserRepo {

    ResponseUser postUser(RequestUser userDto);

    ResponseUser getUserByLogin(String login) throws EntityNotFoundException;

    boolean userExists(String login);
}
