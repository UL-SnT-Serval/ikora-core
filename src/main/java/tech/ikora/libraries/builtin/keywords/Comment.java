package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;

import java.util.Collections;

public class Comment extends LibraryKeyword {
    public Comment(){
        super(Type.LOG, Collections.singletonList(new ListType("items")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
