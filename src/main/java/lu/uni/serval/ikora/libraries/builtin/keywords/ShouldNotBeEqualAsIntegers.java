package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.BooleanType;
import lu.uni.serval.ikora.types.NumberType;
import lu.uni.serval.ikora.types.StringType;

public class ShouldNotBeEqualAsIntegers extends LibraryKeyword {
    public ShouldNotBeEqualAsIntegers(){
        super(Type.ASSERTION,
                new NumberType("first"),
                new NumberType("second"),
                new StringType("message", "None"),
                new BooleanType("values", "None"),
                new NumberType("base", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}