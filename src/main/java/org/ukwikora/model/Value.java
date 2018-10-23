package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.StatusResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Value implements StatusResults.Differentiable {
    public enum Type{
        String, Object, Keyword, Locator, Condition, Keywords, Kwargs
    }

    static private Pattern isVariablePattern;
    static private Pattern hasVariablePattern;
    static private Pattern escapePattern;

    static {
        isVariablePattern = Pattern.compile("^(([@$&])\\{)(.*?)(})$");
        hasVariablePattern = Pattern.compile("(([@$&])\\{)(.*?)(})");
        escapePattern = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");
    }

    private String value;
    private Pattern match;
    private Map<String, Variable> variables;

    Value(String value) {
        this.value = value;
        this.variables = new HashMap<>();

        buildRegex();
    }

    public void setVariable(String name, Variable value) {
        this.variables.put(name, value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public String getName(){
        return toString();
    }

    public boolean matches(String string) {
        Matcher matcher = match.matcher(string);
        return matcher.matches();
    }

    public boolean isVariable() {
        Matcher matcher = getVariableMatcher(this.value, true);
        return matcher.matches();
    }

    public static boolean isVariable(String text) {
        Value value = new Value(text);
        return value.hasVariable();
    }

    public boolean hasVariable() {
        Matcher matcher = getVariableMatcher(this.value, false);
        return matcher.matches();
    }

    static public boolean hasVariable(String text) {
        Value value = new Value(text);
        return value.hasVariable();
    }

    public List<String> findVariables() {
        List<String> variables = new ArrayList<>();

        Matcher matcher = getVariableMatcher(this.value, false);

        while (matcher.find()){
            variables.add(this.value.substring(matcher.start(), matcher.end()));
        }

        return variables;
    }

    public static List<String> findVariables(String text) {
        Value value = new Value(text);
        return value.findVariables();
    }

    private void buildRegex() {
        Matcher matcher = getVariableMatcher(this.value, false);

        String placeholder = "@@@@___VARIABLE__PLACEHOLDER___@@@@";

        String pattern = matcher.replaceAll(placeholder).trim();
        pattern = escape(pattern);
        pattern = pattern.replaceAll(placeholder, "(.*)");

        match = Pattern.compile("^" + pattern + "$", Pattern.CASE_INSENSITIVE);
    }

    public static Matcher getVariableMatcher(String value, boolean strict) {
        if(strict){
            return isVariablePattern.matcher(value);
        }

        return hasVariablePattern.matcher(value);
    }

    public static String escape(String s){
        return escapePattern.matcher(s).replaceAll("\\\\$0");
    }

    @Override
    public double distance(StatusResults.Differentiable other) {
        if(!(other instanceof Value)){
            return 1;
        }

        Value value = (Value)other;
        return this.value.equals(value.value) ? 0 : 1;
    }

    @Override
    public List<Action> differences(StatusResults.Differentiable other) {
        return null;
    }
}