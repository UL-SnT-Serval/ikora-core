package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.KeywordType;

public class RunKeywordAndContinueOnFailure extends LibraryKeyword {
    public RunKeywordAndContinueOnFailure(){
        super(Type.CONTROL_FLOW, new KeywordType("keyword"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
