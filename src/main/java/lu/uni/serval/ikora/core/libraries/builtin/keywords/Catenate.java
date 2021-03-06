package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;

public class Catenate extends LibraryKeyword {
    public Catenate() {
        super(Type.SET, new ListType("items"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
