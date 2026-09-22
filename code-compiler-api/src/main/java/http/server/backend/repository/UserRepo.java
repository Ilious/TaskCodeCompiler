package http.server.backend.repository;

import http.server.backend.model.user.UserDto;
import http.server.backend.repository.interfaces.IUserRepo;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepo implements IUserRepo {

    private final Map<Long, UserDto> storage = new HashMap<>();

    @Override
    public UserDto postUser(UserDto userDto) {
        storage.put(userDto.id(), userDto);

        return userDto;
    }

    @Override
    public UserDto getUserByLogin(String login) throws EntityNotFoundException {
        UserDto userDtoByName = storage.values()
                .stream()
                .filter(u -> u.login().equals(login))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(login, "user"));

        return new UserDto(userDtoByName.id(),
                userDtoByName.login(),
                userDtoByName.password());
    }

    @Override
    public boolean userExists(String login) {
        return storage.values()
                .stream()
                .anyMatch(u -> u.login().equals(login));
    }
}
