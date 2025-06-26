package http.server.backend.service.interfaces;

import http.server.backend.model.api.Session;
import http.server.backend.model.User;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

public interface ISessionService {
    Session createSession(User user);

    boolean validateSession(String sessionId);

    void removeSession(String sessionId);

    Long getUserId(String token);

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    void clearExpiredSessions();
}
