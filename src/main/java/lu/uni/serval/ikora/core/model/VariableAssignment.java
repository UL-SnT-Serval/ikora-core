package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.utils.LevenshteinDistance;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;

public class VariableAssignment extends SourceNode implements Dependable{
    private final Variable variable;
    private final NodeList<Argument> values;
    private final Set<SourceNode> dependencies;

    public VariableAssignment(Variable variable){
        this.variable = variable;
        this.values = new NodeList<>();
        this.dependencies = new HashSet<>();
        this.addTokens(variable.getTokens());

        addAstChild(variable);
    }

    public void addValue(SourceNode value) {
        this.addTokens(value.getTokens());
        this.values.add(new Argument(value));
    }

    public Variable getVariable(){
        return variable;
    }

    public NodeList<Argument> getValues() {
        return this.values;
    }

    @Override
    public boolean matches(Token name) {
        return variable.matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        throw new NotImplementedException("Runner is not implemented yet");
    }

    @Override
    public Token getNameToken() {
        return variable.getNameToken();
    }

    @Override
    public double distance(SourceNode other) {
        if(other == this){
            return 0;
        }

        if(!(other instanceof VariableAssignment)){
            return 1;
        }

        VariableAssignment assignment = (VariableAssignment)other;

        double distanceName = this.getNameToken().equalsIgnorePosition(assignment.getNameToken()) ? 0. : 0.5;
        double distanceValues = LevenshteinDistance.index(this.getValues(), assignment.getValues()) == 0. ? 0. : 0.5;

        return distanceName + distanceValues;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof VariableAssignment)){
            return Collections.singletonList(Edit.changeType(this, other));
        }

        VariableAssignment assignment = (VariableAssignment)other;

        List<Edit> edits = new ArrayList<>();

        if(!this.getName().equals(assignment.getName())){
            edits.add(Edit.changeName(this, other));
        }

        edits.addAll(LevenshteinDistance.getDifferences(this.getValues(), assignment.getValues()));

        return edits;
    }

    @Override
    public void addDependency(SourceNode node) {
        if(node == null) {
            return;
        }

        this.dependencies.add(node);
    }

    @Override
    public void removeDependency(SourceNode node) {
        this.dependencies.remove(node);
    }

    @Override
    public Set<SourceNode> getDependencies() {
        return dependencies;
    }
}
