package org.ikora.builder;

import org.ikora.model.NodeTable;
import org.ikora.model.LineRange;
import org.ikora.model.Variable;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Optional;

class VariableTableParser {
    private VariableTableParser() {}

    public static NodeTable<Variable> parse(LineReader reader) throws IOException {
        NodeTable<Variable> variableTable = new NodeTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid()){
            if(LexerUtils.isBlock(reader.getCurrent().getText())){
                break;
            }

            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            String[] tokens = LexerUtils.tokenize(reader.getCurrent().getText());

            Optional<Variable> optional = VariableParser.parse(tokens[0]);

            if(!optional.isPresent()){
                throw new InvalidParameterException(tokens[0]);
            }

            Variable variable = optional.get();

            int startLine = reader.getCurrent().getNumber();

            for (int i = 1; i < tokens.length; ++i) {
                variable.addElement(tokens[i]);
            }

            reader.readLine();

            int endLine = reader.getCurrent().getNumber();
            variable.setLineRange(new LineRange(startLine, endLine));

            variableTable.add(variable);
        }

        return variableTable;
    }
}