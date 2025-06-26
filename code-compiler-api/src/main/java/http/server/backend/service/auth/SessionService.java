package http.server.backend.service.auth;

import http.server.backend.model.api.Session;
import http.server.backend.model.User;
import http.server.backend.service.interfaces.ISessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class SessionService implements ISessionService {

    private final Map<String, Session> sessionStorage = new ConcurrentHashMap<>();

    private final int sessionDuration;

    public SessionService(@Value("${session.duration:10}") int sessionTime) {
        sessionDuration = sessionTime;
    }

    @Override
    public Session createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        Session session = Session.builder()
                .id(sessionId)
                .user_id(user.getId())
                .duration(Duration.ofMinutes(sessionDuration))
                .stTime(Instant.now())
                .build();

        sessionStorage.put(sessionId, session);
        return session;
    }

    @Override
    public boolean validateSession(String sessionId) {
        Session session = sessionStorage.getOrDefault(sessionId, null);

        if (session == null)
            return false;

        if (Instant.now().isAfter(session.getStTime().plus(session.getDuration()))) {
            removeSession(sessionId);
            return false;
        }

        return true;
    }

    @Override
    public void removeSession(String sessionId) {
        sessionStorage.remove(sessionId);
    }

    @Override
    public Long getUserId(String token) {
        Session session = sessionStorage.getOrDefault(token, null);
        if (Objects.isNull(session))
            return null;
        return session.getUser_id();
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    @Override
    public void clearExpiredSessions() {
        Set<String> expiredSessions = new HashSet<>();

        for (Session session : sessionStorage.values())
            if (Instant.now().isAfter(session.getStTime().plus(session.getDuration())))
                expiredSessions.add(session.getId());

        for (String sessionId : expiredSessions)
            removeSession(sessionId);
    }
}
