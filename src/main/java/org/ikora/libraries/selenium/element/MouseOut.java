package org.ikora.libraries.selenium.element;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class MouseOut extends LibraryKeyword {
    public MouseOut(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
