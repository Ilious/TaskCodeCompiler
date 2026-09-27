package http.server.backend.service.auth;

import http.server.backend.exceptions.authentication.LoginException;
import http.server.backend.mappers.UserMapper;
import http.server.backend.model.user.ResponseUser;
import http.server.backend.model.user.User;
import http.server.backend.model.user.RequestUser;
import http.server.backend.repository.UserRepoJPA;
import http.server.backend.service.interfaces.IUserService;
import http.server.backend.utils.LoginUtils;
import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if (!existsUserByLogin(user.login()))
            throw new EntityNotFoundException(user.login(), "user");

        User userByLogin = userRepo.getUsersByLogin(user.login());
        String password = userByLogin.getPassword();

        if (!LoginUtils.verifyPassword(user.password(), password))
            throw new LoginException("user is not registered in system", user.password());

        return userMapper.toDto(userByLogin);
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.getUserById(id);
    }
}
