package org.ikora.libraries.builtin;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ReturnFromKeyword extends LibraryKeyword {
    public ReturnFromKeyword(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
