package http.server.backend.component.codeExecutor;

import org.springframework.stereotype.Component;

@Component
public class CExecutor extends CodeExecutor {

    @Override
    public String[] getCmdParams(String code) {
        return new String[]{"sh", "-c", """
                mkdir -p /runner/c && cd /runner/c &&
                cat << EOF > main.c
                %s
                EOF
                cd /runner/c &&
                gcc main.c -o main &&
                ./main""".formatted(code)};
    }
}
