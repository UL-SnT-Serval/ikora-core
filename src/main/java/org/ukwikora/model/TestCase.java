package org.ukwikora.model;

import org.ukwikora.analytics.VisitorMemory;

public class TestCase extends KeywordDefinition {
    private KeywordCall setup;
    private KeywordCall tearDown;

    public void setSetup(KeywordCall setup){
        this.setup = setup;
    }

    public void setSetup(Step step){
        setSetup(toCall(step));
    }

    public void setTearDown(KeywordCall tearDown){
        this.tearDown = tearDown;
    }

    public void setTearDown(Step tearDown){
        setTearDown(toCall(tearDown));
    }

    public KeywordCall getSetup(){
        return setup;
    }

    public KeywordCall getTearDown(){
        return tearDown;
    }

    @Override
    public void accept(StatementVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    private KeywordCall toCall(Step step){
        if(step == null){
            return null;
        }

        if(KeywordCall.class.isAssignableFrom(step.getClass())){
            return (KeywordCall) step;
        }

        return null;
    }
}
