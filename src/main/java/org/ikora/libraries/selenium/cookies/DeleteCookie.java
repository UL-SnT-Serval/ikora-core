package org.ikora.libraries.selenium.cookies;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class DeleteCookie extends LibraryKeyword {
    public DeleteCookie(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
