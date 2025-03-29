package http.server.backend.service.auth;

import http.server.backend.exceptions.storage.EntityExistsException;
import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.model.User;
import http.server.backend.model.request.RequestUser;
import http.server.backend.repository.interfaces.IUserRepo;
import http.server.backend.service.interfaces.ISessionService;
import http.server.backend.service.interfaces.IUserService;
import http.server.backend.utils.LoginUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements IUserService {

    private final IUserRepo userRepo;

    private long idx;

    private final long MODIFIER;

    private final LoginUtils loginUtils;

    public LoginService(@Value("${entities.user.modifier:5}") long MODIFIER,
                        @Value("${entities.user.stIdx:0}") long idx,
                        IUserRepo userRepo,
                        ISessionService sessionService,
                        LoginUtils loginUtils) {
        this.MODIFIER = MODIFIER;
        this.idx = idx;
        this.userRepo = userRepo;
        this.loginUtils = loginUtils;
    }

    private long updIdx() {
        idx += MODIFIER;
        return idx;
    }

    @Override
    public boolean existsUserByLogin(String login) {
        return userRepo.userExists(login);
    }

    @Override
    public User createUser(RequestUser user) throws EntityExistsException {
        if (existsUserByLogin(user.login()))
            throw new EntityExistsException(user.login(), "user");
        String encodedPassword = loginUtils.encodePassword(user.password());
        User encodedUser = new User(updIdx(), user.login(), encodedPassword);

        return userRepo.postUser(encodedUser);
    }

    @Override
    public User loginUser(RequestUser user) {
        if (!existsUserByLogin(user.login()))
            throw new EntityNotFoundException(user.login(), "user");

        String password = userRepo.getUserByLogin(user.login()).getPassword();

        if (!loginUtils.verifyPassword(user.password(), password))
            throw new AuthenticationServiceException("user is not registered in system");

        return userRepo.getUserByLogin(user.login());
    }
}
