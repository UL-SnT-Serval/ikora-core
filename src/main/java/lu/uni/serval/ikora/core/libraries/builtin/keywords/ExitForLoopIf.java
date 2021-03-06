package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ConditionType;

public class ExitForLoopIf extends LibraryKeyword {
    public ExitForLoopIf(){
        super(Type.CONTROL_FLOW, new ConditionType("condition"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
