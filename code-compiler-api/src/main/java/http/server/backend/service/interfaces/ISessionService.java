package http.server.backend.service.interfaces;

import http.server.backend.model.session.SessionDto;
import http.server.backend.model.user.ResponseUser;

import java.util.UUID;

public interface ISessionService {
    SessionDto createSession(ResponseUser userDto);

    boolean validateSession(UUID sessionId);

    void removeSession(UUID sessionId);

    Long getUserId(UUID token);
}
