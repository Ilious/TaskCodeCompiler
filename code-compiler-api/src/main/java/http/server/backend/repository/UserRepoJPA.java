package http.server.backend.repository;

import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepoJPA extends JpaRepository<User, Long> {

    boolean existsUserByLogin(String login);

    Optional<User> findFirstByLogin(String login);

    default User getUsersByLogin(String login) {
        return findFirstByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(login, "user"));
    }

    default User getUserById(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id), "user"));
    }
}
