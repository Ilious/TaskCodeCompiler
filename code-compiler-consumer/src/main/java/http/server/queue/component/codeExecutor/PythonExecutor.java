package http.server.queue.component.codeExecutor;

import org.springframework.stereotype.Component;

@Component
public class PythonExecutor extends CodeExecutor {

    @Override
    public String[] getCmdParams(String code) {
        return new String[]{"python3", "-c", code};
    }
}
