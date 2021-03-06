package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.types.StringType;

import java.util.regex.Pattern;

public class Space extends LibraryVariable {
    private Pattern match;

    public Space(){
        super(new StringType("SPACE"), Format.SCALAR);
        match = Pattern.compile("^\\$\\{SPACE(\\s*\\*\\s*\\d*)?}$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean matches(Token name) {
        return match.matcher(name.getText()).matches();
    }
}
