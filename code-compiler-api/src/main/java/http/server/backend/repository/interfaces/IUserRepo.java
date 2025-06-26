package http.server.backend.repository.interfaces;

import http.server.backend.model.User;
import http.server.backend.exceptions.storage.EntityNotFoundException;

public interface IUserRepo {

    User postUser(User user);

    User getUserByLogin(String login) throws EntityNotFoundException;

    boolean userExists(String login);
}
