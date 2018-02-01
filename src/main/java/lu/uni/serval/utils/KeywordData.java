package lu.uni.serval.utils;

import lu.uni.serval.robotframework.model.Argument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordData {
    public String name;
    public List<String> arguments;
    public Map<String, List<String>> variables;
    public String file;
    public String documentation;

    public KeywordData(){
        this.arguments = new  ArrayList<String>();
        this.variables =  new HashMap<String, List<String>>();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(String argument : arguments) {
            stringBuilder.append("\t");
            stringBuilder.append(cleanArgument(argument));
        }

        return this.name + stringBuilder.toString();
    }

    private String cleanArgument(String argument) {
        if (!Argument.hasVariable(argument)) {
            return argument;
        }

        List<String> variables;

         do {
             variables = Argument.findVariables(argument);

            for(String variable : variables) {
                argument = argument.replace(variable, resolveVariable(variable));
            }

         } while (!variables.isEmpty());

         return argument;
    }

    private String resolveVariable(String variable) {
        List<String> values = variables.get(variable);

        String returnValue;

        if(values == null || values.isEmpty()) {
            returnValue = variable.replace("${", "[");
            returnValue = returnValue.replace("}", "]");
        }
        else if (values.size() == 1) {
            String value = values.get(0);

            if(Argument.hasVariable(value)) {
                value = cleanArgument(value);
            }

            returnValue = value;
        }
        else
        {
            StringBuilder stringBuilder = new StringBuilder();

            for(String value : values) {
                stringBuilder.append("\t");
                stringBuilder.append(resolveVariable(value));
            }

            returnValue = stringBuilder.toString().trim();
        }

        return returnValue;
    }
}