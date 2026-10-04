package http.server.backend.service.auth;

import http.server.backend.exceptions.authentication.LoginException;
import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.mappers.UserMapper;
import http.server.backend.model.user.RequestUser;
import http.server.backend.model.user.ResponseUser;
import http.server.backend.model.user.User;
import http.server.backend.repository.UserRepoJPA;
import http.server.backend.service.interfaces.IUserService;
import http.server.backend.utils.LoginUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService implements IUserService {

    private final UserRepoJPA userRepo;

    private final UserMapper userMapper;

    @Override
    public boolean existsUserByLogin(String login) {
        return userRepo.existsUserByLogin(login);
    }

    @Override
    public ResponseUser createUser(RequestUser user) throws EntityExistsException {
        if (existsUserByLogin(user.login()))
            throw new EntityExistsException(user.login(), "user");

        User encodedUser = userMapper.toEntity(user);

        return userMapper.toDto(userRepo.save(encodedUser));
    }

    @Override
    public ResponseUser loginUser(RequestUser user) {
        Optional<User> userByLogin = userRepo.findFirstByLogin(user.login());

        if (userByLogin.isEmpty() || !LoginUtils.verifyPassword(
                user.password(),
                userByLogin.get().getPassword()
        )) {
            throw new LoginException("Error wrong login or password", user.login());
        }

        return userMapper.toDto(userByLogin.get());
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.getUserById(id);
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepo.getUsersByLogin(login);
    }
}
