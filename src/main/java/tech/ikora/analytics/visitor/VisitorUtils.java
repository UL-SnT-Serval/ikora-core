package tech.ikora.analytics.visitor;
import tech.ikora.model.*;


class VisitorUtils {
    private VisitorUtils(){}

    static void traverseDependencies(NodeVisitor visitor, Node node, VisitorMemory memory){
        for(Node dependency: node.getDependencies()){
            if(memory.isAcceptable(dependency)){
                dependency.accept(visitor, memory.getUpdated(dependency));
            }
        }
    }

    static void traverseSteps(NodeVisitor visitor, KeywordDefinition keyword, VisitorMemory memory){
        for(Step step: keyword.getSteps()){
            if(memory.isAcceptable(step)){
                step.accept(visitor, memory.getUpdated(step));
            }
        }
    }

    static void traverseForLoopSteps(NodeVisitor visitor, ForLoop forLoop, VisitorMemory memory){
        for(Step step: forLoop.getSteps()){
            if(memory.isAcceptable(step)){
                step.accept(visitor, memory.getUpdated(step));
            }
        }
    }

    static void traverseAssignmentCall(NodeVisitor visitor, Assignment assignment, VisitorMemory memory){
        assignment.getKeywordCall().flatMap(KeywordCall::getKeyword).ifPresent(keyword ->
                keyword.accept(visitor, memory.getUpdated(assignment))
        );
    }

    static void traverseKeywordCall(NodeVisitor visitor, KeywordCall call, VisitorMemory memory){
        call.getKeyword().ifPresent(keyword ->{
            for(Argument argument: call.getArgumentList()){
                argument.accept(visitor, memory.getUpdated(argument));
            }

            keyword.accept(visitor, memory.getUpdated(keyword));
        });
    }

    static void traverseArgument(NodeVisitor visitor, Argument argument, VisitorMemory memory){
        argument.getDefinition().ifPresent(keyword ->
                keyword.accept(visitor, memory.getUpdated(argument))
        );
    }
}