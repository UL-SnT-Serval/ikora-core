package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueResolver;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BaseType;

import java.util.HashSet;
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

        String patternString = ValueResolver.escape(getName());
        patternString = ValueResolver.getGenericVariableName(patternString);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public String getName() {
        String prefix = "$";

        switch (this.format) {
            case SCALAR: prefix = "$"; break;
            case LIST: prefix = "@"; break;
            case DICTIONARY: prefix = "&"; break;
        }

        return String.format("%s{%s}", prefix, this.type.getName());
    }

    @Override
    public String getLibraryName() {
        return "builtin";
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
        String generic = ValueResolver.getGenericVariableName(name.getText());

        Matcher matcher = pattern.matcher(generic);
        return matcher.matches();
    }

    @Override
    public void execute(Runtime runtime) throws Exception {

    }
}
