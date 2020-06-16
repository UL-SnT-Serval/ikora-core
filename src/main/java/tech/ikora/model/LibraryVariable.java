package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BaseType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LibraryVariable implements Node {
    protected enum Format{
        SCALAR, LIST, DICTIONARY
    }

    protected final BaseType type;
    protected final Format format;
    protected final Pattern pattern;

    private final Set<Node> dependencies;

    protected LibraryVariable(BaseType type, Format format){
        this.type = type;
        this.format = format;

        this.dependencies = new HashSet<>();

        String patternString = ValueLinker.escape(getName());
        patternString = ValueLinker.getGenericVariableName(patternString);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public String getName() {
        return toVariable(this.getClass());
    }

    @Override
    public String getLibraryName() {
        return "builtin";
    }

    private static String toVariable(Class<? extends LibraryVariable> variableClass) {
        return String.format("${%s}", variableClass.getSimpleName());
    }

    @Override
    public void addDependency(Node node) {
        this.dependencies.add(node);
    }

    @Override
    public Set<Node> getDependencies() {
        return this.dependencies;
    }

    @Override
    public boolean matches(Token name) {
        String generic = ValueLinker.getGenericVariableName(name.getText());

        Matcher matcher = pattern.matcher(generic);
        return matcher.matches();
    }

    @Override
    public void execute(Runtime runtime) throws Exception {

    }
}
