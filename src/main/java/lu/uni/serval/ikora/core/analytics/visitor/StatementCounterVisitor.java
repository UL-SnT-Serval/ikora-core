package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.Assignment;
import lu.uni.serval.ikora.core.model.ForLoop;
import lu.uni.serval.ikora.core.model.KeywordCall;

public class StatementCounterVisitor extends TreeVisitor {
    private int statementCount;

    public StatementCounterVisitor(){
        statementCount = 0;
    }

    public int getStatementCount(){
        return statementCount;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        ++statementCount;
        super.visit(call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        ++statementCount;
        super.visit(assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        ++statementCount;
        super.visit(forLoop, memory);
    }
}
