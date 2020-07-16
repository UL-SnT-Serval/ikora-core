package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ListType;
import tech.ikora.types.PathType;

public class ImportVariables extends LibraryKeyword {
    public ImportVariables(){
        super(Type.CONFIGURATION,
                new PathType("path"),
                new ListType("args")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
