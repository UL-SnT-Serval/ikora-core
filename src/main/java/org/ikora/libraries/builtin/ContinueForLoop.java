package org.ikora.libraries.builtin;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ContinueForLoop extends LibraryKeyword {
    public ContinueForLoop(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
