package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.NodeVisitor;
import org.ikora.analytics.VisitorMemory;
import org.ikora.runner.Runtime;
import org.ikora.utils.LevenshteinDistance;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ForLoop extends Step {
    private List<Step> steps;
    private List<Value> parameters;

    public ForLoop() {
        steps = new ArrayList<>();
        parameters = new ArrayList<>();
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    @Override
    public Keyword getStep(int position) {
        if(steps.size() <= position){
            return null;
        }

        return steps.get(position);
    }

    public List<Step> getSteps(){
        return steps;
    }

    @Override
    public List<Value> getParameters() {
        return parameters;
    }

    @Override
    public Optional<Value> getParameter(int position, boolean resolved) {
        return Optional.empty();
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ForLoop)){
            return false;
        }

        if(!super.equals(other)) {
            return false;
        }

        ForLoop forLoop = (ForLoop)other;

        boolean same = this.steps.size() == forLoop.steps.size();

        for(int i = 0; same && i < this.steps.size(); ++i) {
            same &= this.steps.get(i).equals(forLoop.steps.get(i));
        }

        return  same;
    }

    @Override
    public boolean hasKeywordParameters() {
        return false;
    }

    @Override
    public List<KeywordCall> getKeywordParameter() {
        return Collections.emptyList();
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.empty();
    }

    @Override
    public void execute(Runtime runtime) {
        throw new NotImplementedException("Didn't implemented the execution module yet");
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[0];
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        if(!(other instanceof ForLoop)){
            return 1;
        }

        ForLoop forLoop = (ForLoop)other;

        return LevenshteinDistance.index(steps, forLoop.steps);
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof Step)){
            return actions;
        }

        if(this.getClass() != other.getClass()){
            actions.add(Action.changeStepType(this, other));
        }
        else{
            ForLoop forLoop = (ForLoop)other;

            if(LevenshteinDistance.stringIndex(this.getName(), forLoop.getName()) > 0){
                actions.add(Action.changeForLoopCondition(this, forLoop));
            }

            if(LevenshteinDistance.index(this.getSteps(), forLoop.getSteps()) > 0){
                actions.add(Action.changeForLoopBody(this, forLoop));
            }
        }

        return actions;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public List<Value> getReturnValues() {
        return Collections.emptyList();
    }
}