package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

import java.util.Collections;

public class ConvertToBoolean extends LibraryKeyword {
    public ConvertToBoolean(){
        super(Type.UNKNOWN, Collections.singletonList(new StringType("item")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
