package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.PathType;

public class Curdir extends LibraryVariable {
    public Curdir(){
        super(new PathType("CURDIR"), Format.SCALAR);
    }
}
