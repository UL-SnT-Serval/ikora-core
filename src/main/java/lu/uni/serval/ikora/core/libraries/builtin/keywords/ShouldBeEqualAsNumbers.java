package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.NumberType;
import lu.uni.serval.ikora.core.types.StringType;

public class ShouldBeEqualAsNumbers extends LibraryKeyword {
    public ShouldBeEqualAsNumbers(){
        super(Type.ASSERTION,
                new NumberType("first"),
                new NumberType("second"),
                new StringType("message", "None"),
                new BooleanType("values", "None"),
                new NumberType("precision", "6")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
