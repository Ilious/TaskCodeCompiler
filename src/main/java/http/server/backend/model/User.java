package http.server.backend.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    @NonNull
    private String login;

    @NonNull
    private String password;
}
