package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.StringType;

public class GetVariableValue extends LibraryKeyword {
    public GetVariableValue(){
        super(Type.GET,
                new StringType("name"),
                new StringType("default", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}