package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.LineNumberReader;
import java.io.IOException;

public class TestCaseTableParser {
    private TestCaseTableParser() {}

    static public Line parse(LineNumberReader reader, TestCaseTable testCaseTable) throws IOException {
        Line line = Line.getNextLine(reader);

        while(line.isValid()){
            if(Utils.isBlock(line.getText())){
                break;
            }

            if(line.isEmpty()){
                continue;
            }

            line = TestCaseParser.parse(reader, line, testCaseTable);
        }

        return line;
    }
}
