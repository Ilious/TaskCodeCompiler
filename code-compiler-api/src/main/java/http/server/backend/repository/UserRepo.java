package http.server.backend.repository;

import http.server.backend.model.User;
import http.server.backend.repository.interfaces.IUserRepo;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepo implements IUserRepo {

    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public User postUser(User user) {
        storage.put(user.getId(), user);

        return user;
    }

    @Override
    public User getUserByLogin(String login) throws EntityNotFoundException {
        User userByName = storage.values()
                .stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(login, "user"));

        return new User(userByName.getId(),
                userByName.getLogin(),
                userByName.getPassword());
    }

    @Override
    public boolean userExists(String login) {
        return storage.values()
                .stream()
                .anyMatch(u -> u.getLogin().equals(login));
    }
}
