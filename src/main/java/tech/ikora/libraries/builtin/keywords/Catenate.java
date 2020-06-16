package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;

import java.util.Collections;

public class Catenate extends LibraryKeyword {
    public Catenate() {
        super(Type.UNKNOWN, Collections.singletonList(new ListType("items")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
