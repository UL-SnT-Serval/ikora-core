package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.builder.Line;

public class Range {
    private final Pointer start;
    private final Pointer end;

    public Range(Pointer start, Pointer end) {
        this.start = start;
        this.end = end;
    }

    public Pointer getStart() {
        return start;
    }

    public Pointer getEnd() {
        return end;
    }

    public boolean isValid(){
        return start != null && end != null;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", start.toString(), end.toString());
    }

    public static Range createInvalid(){
        return new Range(null, null);
    }

    public static Range fromTokens(Token startToken, Token endToken, Line line) {
        if(startToken == null || endToken == null || startToken.isEmpty() || endToken.isEmpty()){
            return Range.fromLine(line);
        }

        Pointer start = new Pointer(startToken.getLine(), startToken.getStartOffset());
        Pointer end = new Pointer(endToken.getLine(), endToken.getEndOffset());

        return new Range(start, end);
    }

    public static Range fromTokens(Tokens tokens, Line line) {
        return Range.fromTokens(tokens.first(), tokens.last(), line);
    }

    public static Range fromToken(Token token, Line line){
        return Range.fromTokens(token, token, line);
    }

    public static Range fromLine(Line line) {
        if(line == null){
            return Range.createInvalid();
        }

        Pointer start = new Pointer(line.getNumber(), 0);
        Pointer end = new Pointer(line.getNumber(), line.getText().length());

        return new Range(start, end);
    }
}
