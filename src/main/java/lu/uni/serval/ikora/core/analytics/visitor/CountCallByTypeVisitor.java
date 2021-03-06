package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.KeywordCall;

public class CountCallByTypeVisitor extends TreeVisitor {
    private final Keyword.Type type;
    private int count;

    public CountCallByTypeVisitor(Keyword.Type type){
        this.type = type;
    }

    public int getCount(){
        return count;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        call.getKeyword().ifPresent(k -> {
            if(k.getType() == type){
                ++this.count;
            }
        });

        super.visit(call, memory);
    }
}
