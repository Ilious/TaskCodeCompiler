package http.server.backend.repository;

import http.server.backend.exceptions.storage.EntityNotFoundException;
import http.server.backend.model.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface SessionRepoJPA extends JpaRepository<Session, UUID> {

    default Session getSessionById(UUID id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString(), "user"));
    }

    @Modifying
    @Query("DELETE Session s WHERE s.expiresAt < :now")
    int deleteExpired(@Param("now") Instant now);

    void removeById(UUID id);
}
