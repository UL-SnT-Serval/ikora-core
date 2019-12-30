package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.runner.Runtime;

import java.util.*;

public abstract class LibraryKeyword extends Keyword {
    protected Type type;

    public LibraryKeyword() {
        this.type = Type.Unknown;
    }

    public Type getType(){
        return this.type;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public String getName(){
        return toKeyword(this.getClass());
    }

    @Override
    public Value getNameAsValue(){
        return new Value(toKeyword(this.getClass()));
    }

    public static String toKeyword(Class<? extends LibraryKeyword> libraryClass) {
        String name = libraryClass.getSimpleName();
        return name.replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }

    @Override
    public double distance(Differentiable other){
        if(other == null){
            return 1;
        }

        return other.getClass() == this.getClass() ? 0 : 1;
    }

    @Override
    public List<Action> differences(Differentiable other){
        if(other != null && other.getClass() == this.getClass()){
            return Collections.emptyList();
        }

        return Collections.singletonList(Action.invalid(this, other));
    }

    @Override
    public String getLibraryName(){
        String[] packages = this.getClass().getCanonicalName().split("\\.");

        for(int i = 0; i < packages.length; ++i){
            if(packages[i].equalsIgnoreCase("libraries") && i < packages.length - 1){
                return packages[i + 1];
            }
        }

        return "";
    }

    @Override
    public boolean matches(String name) {
        return this.getName().matches(name);
    }

    public Value.Type[] getArgumentTypes() {
        return new Value.Type[0];
    }

    @Override
    public void execute(Runtime runtime) throws Exception{
        runtime.enterNode(this);

        run(runtime);

        runtime.exitNode(this);
    }

    protected abstract void run(Runtime runtime);

    public String getDocumentation(){
        return "";
    }

    @Override
    public List<Value> getReturnValues(){
        return Collections.emptyList();
    }

    @Override
    public int getMaxNumberArguments() {
        Value.Type[] types = getArgumentTypes();

        if(types.length == 0){
            return 0;
        }

        if(types[types.length - 1] == Value.Type.Kwargs){
            return -1;
        }

        return types.length;
    }
}
