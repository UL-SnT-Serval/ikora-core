package tech.ikora.builder;

import tech.ikora.Helpers;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.model.TimeOut;
import tech.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TimeoutParserTest {
    @Test
    void testParseValidTimeoutWithRawValue() throws IOException, InvalidArgumentException, MalformedVariableException {
        TimeOut timeout = parse("[TimeOut]  1 minute 30 seconds");
        assertTrue(timeout.isValid());
        assertEquals("1 minute 30 seconds", timeout.getName());
    }

    @Test
    void testParseValidTimeoutWithVariable() throws IOException, InvalidArgumentException, MalformedVariableException {
        TimeOut timeout = parse("[TimeOut]  ${value}");
        assertTrue(timeout.isValid());
        assertEquals("${value}", timeout.getName());
    }

    @Test
    void testParseNoneTimeout() throws IOException, InvalidArgumentException, MalformedVariableException {
        TimeOut timeout = parse("[TimeOut]  None");
        assertTrue(timeout.isValid());
        assertEquals("None", timeout.getName());
    }

    @Test
    void testParseInvalidTimeout() {
        assertThrows(InvalidArgumentException.class, () -> parse("[TimeOut]  Invalid timeout"));
    }

    private static TimeOut parse(String text) throws IOException, InvalidArgumentException, MalformedVariableException {
        LineReader reader = Helpers.lineReader(text);
        Tokens tokens = LexerUtils.tokenize(reader);

        return TimeoutParser.parse(tokens.withoutTag("\\[TimeOut\\]"));
    }
}
