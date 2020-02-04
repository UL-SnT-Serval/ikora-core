package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.*;

import java.io.IOException;

public class KeywordCallParser {
    private KeywordCallParser() {}

    public static KeywordCall parse(LineReader reader, Tokens tokens, boolean allowGherkin, ErrorManager errors) {
        Tokens callTokens = tokens.withoutIndent();

        if(callTokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Position.fromLine(reader.getCurrent())
            );
        }
        else{
            Token rawName = callTokens.first();
            Token name = getKeywordCallName(rawName, allowGherkin);
            Gherkin gherkin = new Gherkin(rawName);

            KeywordCall call = new KeywordCall(name);
            call.setGherkin(gherkin);

            for(Token token: callTokens.withoutFirst()) {
                try {
                    Argument argument = new Argument(call, token);
                    call.addArgument(argument);
                } catch (InvalidDependencyException e) {
                    errors.registerInternalError(
                            reader.getFile(),
                            "Failed to register parameter to keyword call",
                            Position.fromToken(token, reader.getCurrent())
                    );
                }
            }

            return call;
        }

        return null;
    }

    private static Token getKeywordCallName(Token raw, boolean allowGherkin) {
        if(!allowGherkin){
            return raw;
        }

        return Gherkin.getCleanName(raw);
    }
}