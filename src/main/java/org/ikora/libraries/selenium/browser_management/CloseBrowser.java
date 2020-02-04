package org.ikora.libraries.selenium.browser_management;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class CloseBrowser extends LibraryKeyword {
    public CloseBrowser(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}