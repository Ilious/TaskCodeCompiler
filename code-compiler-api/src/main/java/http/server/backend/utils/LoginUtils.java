package http.server.backend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginUtils {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static boolean verifyPassword(String raw, String encoded) {
        return PASSWORD_ENCODER.matches(raw, encoded);
    }

    public static String encodePassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }
}
