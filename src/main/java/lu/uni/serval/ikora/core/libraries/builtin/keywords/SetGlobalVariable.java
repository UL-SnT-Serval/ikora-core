package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.StringType;

import java.util.List;

public class SetGlobalVariable extends LibraryKeyword implements ScopeModifier {
    public SetGlobalVariable(){
        super(Type.SET,
                new StringType("name"),
                new ListType("values")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) {
        List<Argument> argumentList = call.getArgumentList();

        if(argumentList.size() < 2){
            runtime.getErrors().registerInternalError(
                    call.getSource(),
                    "Failed to update global scope: not enough arguments found. Need 2!",
                    call.getRange()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getNameToken());
                runtime.addToGlobalScope(variable);
            } catch (Exception e) {
                runtime.getErrors().registerInternalError(
                        call.getSource(),
                        "Failed to update global scope: malformed variable.",
                        call.getRange()
                );
            }
        }
    }
}
