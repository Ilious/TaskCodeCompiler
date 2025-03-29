package http.server.backend.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginUtils {

    private final PasswordEncoder passwordEncoder;

    public LoginUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean verifyPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
