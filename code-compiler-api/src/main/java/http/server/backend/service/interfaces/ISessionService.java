package http.server.backend.service.interfaces;

import http.server.backend.model.session.SessionDto;
import http.server.backend.model.user.UserDto;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

public interface ISessionService {
    SessionDto createSession(UserDto userDto);

    boolean validateSession(String sessionId);

    void removeSession(String sessionId);

    Long getUserId(String token);

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    void clearExpiredSessions();
}
