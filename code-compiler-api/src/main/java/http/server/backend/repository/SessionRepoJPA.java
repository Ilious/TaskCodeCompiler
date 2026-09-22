package http.server.backend.repository;

import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.model.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepoJPA extends JpaRepository<Session, String> {

    void removeById(String id);

    default Session getSessionById(String id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "user"));
    }
}
