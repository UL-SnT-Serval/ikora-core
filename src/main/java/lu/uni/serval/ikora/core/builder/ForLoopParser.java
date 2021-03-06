package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForLoopParser {
    private ForLoopParser() {}

    public static ForLoop parse(LineReader reader, Tokens tokens, ErrorManager errors) throws IOException {
        Tokens loopTokens = tokens.withoutIndent();

        Token name = extractName(reader, loopTokens, errors);
        Variable iterator = extractIterator(reader, loopTokens, errors);
        Step range = extractRange(reader, loopTokens, errors);

        List<Step> steps = new ArrayList<>();

        while (reader.getCurrent().isValid()){
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            if(LexerUtils.exitBlock(tokens, reader)){
                break;
            }

            Tokens stepTokens = LexerUtils.tokenize(reader);

            Step step = StepParser.parse(reader, stepTokens, false, errors);
            steps.add(step);
        }

        return new ForLoop(name, iterator, range, steps);
    }

    private static Token extractName(LineReader reader, Tokens loop, ErrorManager errors) {

        if(loop.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.FAILED_TO_PARSE_FORLOOP,
                    Range.fromLine(reader.getCurrent())
            );

            return Token.empty();
        }

        return loop.withoutIndent().first();
    }

    private static Variable extractIterator(LineReader reader, Tokens loop, ErrorManager errors) {
        Variable variable = Variable.invalid();

        if(loop.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.FAILED_TO_LOCATE_ITERATOR_IN_FOR_LOOP,
                    Range.fromLine(reader.getCurrent())
            );
        }
        else{
            try {
                variable = Variable.create(loop.withoutIndent().get(1));
            } catch (MalformedVariableException e) {
                errors.registerInternalError(
                        reader.getSource(),
                        ErrorMessages.FAILED_TO_CREATE_ITERATOR_IN_FOR_LOOP,
                        Range.fromToken(loop.withoutIndent().get(1), reader.getCurrent())
                );
            }
        }

        return variable;
    }

    private static Step extractRange(LineReader reader, Tokens loop, ErrorManager errors) {
        Tokens rangeTokens = loop.withoutIndent().withoutFirst(2);

        Step step = new InvalidStep(rangeTokens.first());

        if(rangeTokens.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Range.fromTokens(loop.withoutIndent(), reader.getCurrent())
            );
        }
        else{
            Tokens cleanTokens = cleanInKeyword(rangeTokens);
            step = KeywordCallParser.parse(reader, cleanTokens, false, errors);
        }

        return step;
    }

    private static Tokens cleanInKeyword(Tokens tokens){
        boolean first = true;

        Tokens cleanTokens = new Tokens();
        for(Token token: tokens){
            if(first){
                first = false;
                String value = token.getText();

                if(StringUtils.compareNoCase(value, "^IN(\\s?)(.+)")){
                    String cleanValue = value.replaceAll("^([Ii])([Nn])", "").trim();
                    cleanTokens.add(new Token(cleanValue, token.getLine(), token.getStartOffset(), token.getEndOffset(), Token.Type.TEXT));
                }
            }
            else{
                cleanTokens.add(token);
            }
        }

        return cleanTokens;
    }
}
