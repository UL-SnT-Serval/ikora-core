package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.analytics.visitor.FindTestCaseVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.StringType;

public class SetTestVariable extends LibraryKeyword implements ScopeModifier {
    public SetTestVariable(){
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
        NodeList<Argument> argumentList = call.getArgumentList();

        if(argumentList.size() < 2){
            runtime.getErrors().registerInternalError(
                    call.getSource(),
                    "Failed to update test scope: no argument found.",
                    call.getRange()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getNameToken());

                FindTestCaseVisitor visitor = new FindTestCaseVisitor();
                call.accept(visitor, new PathMemory());

                for(TestCase testCase: visitor.getTestCases()){
                    runtime.addToTestScope(testCase, variable);
                }
            } catch (Exception e) {
                runtime.getErrors().registerInternalError(
                        call.getSource(),
                        "Failed to update test scope: malformed variable.",
                        call.getRange()
                );
            }
        }
    }
}
