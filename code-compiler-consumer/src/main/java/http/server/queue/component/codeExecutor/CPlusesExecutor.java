package http.server.queue.component.codeExecutor;

import org.springframework.stereotype.Component;

@Component
public class CPlusesExecutor extends CodeExecutor {

    @Override
    public String[] getCmdParams(String code) {
        return new String[]{"sh", "-c", """
                mkdir -p /runner/c++ && cd /runner/c++ &&
                cat << EOF > main.cpp
                %s
                EOF
                cd /runner/c++ &&
                clang++ main.cpp -o main &&
                ./main""".formatted(code)};
    }
}
