package http.server.backend.repository;

import http.server.backend.model.user.RequestUser;
import http.server.backend.model.user.ResponseUser;
import http.server.backend.repository.interfaces.IUserRepo;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * In memory storage for User. Deprecated due to injection DB driver.
 *
 * @deprecated Use {@link UserRepoJPA} for JPA DB instead of it.
 */
@Deprecated(forRemoval = true)
@Repository
public class UserRepo implements IUserRepo {

    private final Map<String, ResponseUser> storage = new HashMap<>();

    @Override
    public ResponseUser postUser(RequestUser userDto) {
        ResponseUser responseUser = new ResponseUser(getNextIdx(), userDto.login());

        storage.put(userDto.login(), responseUser);

        return responseUser;
    }

    @Override
    public ResponseUser getUserByLogin(String login) throws EntityNotFoundException {
        return storage.values()
                .stream()
                .filter(u -> u.login().equals(login))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(login, "user"));
    }

    @Override
    public boolean userExists(String login) {
        return storage.values()
                .stream()
                .anyMatch(u -> u.login().equals(login));
    }

    private Long getNextIdx() {
        return storage.values()
                .stream()
                .map(ResponseUser::id)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}
