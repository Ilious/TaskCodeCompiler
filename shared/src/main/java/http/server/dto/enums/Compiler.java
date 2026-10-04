package http.server.dto.enums;

import lombok.Getter;

@Getter
public enum Compiler {

    Py("py"),
    C("c"),
    CPluses("c++");

    private final String value;

    Compiler(String value) {
        this.value = value;
    }

    public static Compiler from(String parseString) {
        for (Compiler compiler: Compiler.values())
            if (compiler.value.equalsIgnoreCase(parseString))
                return compiler;
        throw new IllegalArgumentException("This compiler doesn't exist");
    }
}
