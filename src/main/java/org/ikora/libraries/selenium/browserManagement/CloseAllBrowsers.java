package org.ikora.libraries.selenium.browserManagement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class CloseAllBrowsers extends LibraryKeyword {
    public CloseAllBrowsers(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
