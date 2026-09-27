package http.server.backend.model.user;


/**
 * Universal dto for user. The reason is deprecated is security issues with response.
 *
 * @deprecated Use {@link RequestUser} for input data and {@link ResponseUser} for output data.
 */
@Deprecated(forRemoval = true)
public record UserDto(
        Long id,

        String login,

        String password) {
}
