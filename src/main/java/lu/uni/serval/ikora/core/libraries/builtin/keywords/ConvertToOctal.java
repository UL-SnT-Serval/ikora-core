package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.NumberType;
import lu.uni.serval.ikora.core.types.StringType;

public class ConvertToOctal extends LibraryKeyword {
    public ConvertToOctal() {
        super(Type.SET,
                new StringType("item"),
                new NumberType("base", "None"),
                new StringType("prefix", "None"),
                new NumberType("length", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
