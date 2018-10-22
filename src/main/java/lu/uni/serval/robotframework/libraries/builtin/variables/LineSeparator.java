package lu.uni.serval.robotframework.libraries.builtin.variables;

import lu.uni.serval.robotframework.model.LibraryVariable;

public class LineSeparator extends LibraryVariable {
    @Override
    public String getName() {
        return "${\\n}";
    }
}