package org.ikora.libraries.selenium.element;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class GetElementAttribute extends LibraryKeyword {
    public GetElementAttribute(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
