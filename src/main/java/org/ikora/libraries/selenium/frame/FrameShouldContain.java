package org.ikora.libraries.selenium.frame;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class FrameShouldContain extends LibraryKeyword {
    public FrameShouldContain(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
