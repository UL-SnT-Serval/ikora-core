package org.ikora.libraries.selenium.frame;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class CurrentFrameContains extends LibraryKeyword {
    public CurrentFrameContains(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}