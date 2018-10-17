package lu.uni.serval.robotframework.model;

public class LineRange {
    private final int start;
    private final int end;

    public LineRange(int start, int end){
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
