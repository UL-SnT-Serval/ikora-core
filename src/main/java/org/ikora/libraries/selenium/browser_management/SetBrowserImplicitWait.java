package org.ikora.libraries.selenium.browser_management;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class SetBrowserImplicitWait extends LibraryKeyword {
    public SetBrowserImplicitWait(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}