package org.ikora.libraries.selenium.formElement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class InputPassword extends LibraryKeyword {
    public InputPassword(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
