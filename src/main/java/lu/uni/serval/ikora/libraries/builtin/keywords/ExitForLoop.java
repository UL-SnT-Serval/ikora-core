package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;

public class ExitForLoop extends LibraryKeyword {
    public ExitForLoop(){
        super(Type.CONTROL_FLOW);
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}