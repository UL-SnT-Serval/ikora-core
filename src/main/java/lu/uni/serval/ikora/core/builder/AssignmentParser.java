package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssignmentParser {
    private AssignmentParser(){}

    public static Assignment parse(LineReader reader, Tokens tokens, ErrorManager errors) {
        List<Variable> returnValues = new ArrayList<>();
        KeywordCall expression = null;

        int offset = 0;
        boolean leftSide = true;
        Tokens assignmentTokens = tokens.withoutIndent();
        for(Token token: assignmentTokens){
            Token clean = VariableAssignmentParser.trimEquals(token);

            if(!clean.isEmpty()){
                if(leftSide && ValueResolver.isVariable(clean)){
                    Optional<Variable> variable = VariableParser.parse(clean);

                    if(variable.isPresent()){
                        Variable returnValue = variable.get();
                        returnValues.add(returnValue);
                    }
                }
                else if(!leftSide){
                    expression = KeywordCallParser.parse(reader, assignmentTokens.withoutFirst(offset), false, errors);
                    break;
                }
            }

            leftSide &= !token.getText().contains("=");
            ++offset;
        }

        Token name = expression != null ? expression.getNameToken() : Token.empty();
        Assignment assignment = new Assignment(name, returnValues, expression);

        if(!assignment.getKeywordCall().isPresent()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    ErrorMessages.ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND,
                    Range.fromTokens(tokens, reader.getCurrent())
            );
        }

        return assignment;
    }
}
