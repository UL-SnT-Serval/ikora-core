package lu.uni.serval.ikora.core.utils;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    void testCountLines(){
        String emptyBlock = "\n";
        String trailingBlock = "Trailing\n";
        String forwardBlock = "\nForward";
        String bothSideBlock = "\nBothSides\n";
        String twoLinesBlock = "Line1\nLine2";

        assertEquals(0, StringUtils.countLines(emptyBlock));
        assertEquals(1, StringUtils.countLines(trailingBlock));
        assertEquals(1, StringUtils.countLines(forwardBlock));
        assertEquals(1, StringUtils.countLines(bothSideBlock));
        assertEquals(2, StringUtils.countLines(twoLinesBlock));
    }

    @Test
    void testBeautifyName(){
        assertEquals("Lower", StringUtils.toBeautifulName("lower"));
        assertEquals("Upper", StringUtils.toBeautifulName("UPPER"));
        assertEquals("Test Lower Underline", StringUtils.toBeautifulName("test_lower_underline"));
        assertEquals("Test Upper Underline", StringUtils.toBeautifulName("TEST_UPPER_UNDERLINE"));
        assertEquals("Test Lower Dash", StringUtils.toBeautifulName("test-lower-dash"));
        assertEquals("Test Upper Dash", StringUtils.toBeautifulName("TEST-UPPER-DASH"));
        assertEquals("Test Trimming", StringUtils.toBeautifulName("test_trimming_"));
        assertEquals("Test Trimming", StringUtils.toBeautifulName("_test_trimming_"));
        assertEquals("Test Trimming", StringUtils.toBeautifulName("_test_trimming"));
    }

    @Test
    void testBeautifyUrl(){
        try {
            assertEquals("page-1.html", StringUtils.toBeautifulUrl("PAGE-1_", "html"));
            assertEquals("page-1.html", StringUtils.toBeautifulUrl("Page 1", "html"));
            assertEquals("page-1.html", StringUtils.toBeautifulUrl("Page1", "html"));
            assertEquals("page-1.html", StringUtils.toBeautifulUrl("Page_1", "html"));
        } catch (UnsupportedEncodingException e) {
            fail("exception caught: " + e.getMessage());
        }
    }

    @Test
    void testLineTruncate(){
        assertEquals("Some random line", StringUtils.lineTruncate("Some random line", -1));
        assertEquals("", StringUtils.lineTruncate("Some random line", 0));
        assertEquals(".", StringUtils.lineTruncate("Some random line", 1));
        assertEquals("..", StringUtils.lineTruncate("Some random line", 2));
        assertEquals("...", StringUtils.lineTruncate("Some random line", 3));
        assertEquals("Some ra...", StringUtils.lineTruncate("Some random line", 10));
        assertEquals("Some random ...", StringUtils.lineTruncate("Some random line", 15));
        assertEquals("Some random line", StringUtils.lineTruncate("Some random line", 16));
    }

    @Test
    void testTrimRight(){
        assertEquals("${variable}", StringUtils.trimRight("${variable}=", "="));
        assertEquals("${variable}", StringUtils.trimRight("${variable} =", " ="));
        assertEquals("=${variable}", StringUtils.trimRight("=${variable}", "="));
        assertEquals("${variable}=", StringUtils.trimRight("${variable}=", "<"));
    }

    @Test
    void testTrimLeft(){
        assertEquals("{variable}", StringUtils.trimLeft("${variable}", "$"));
        assertEquals("{variable}", StringUtils.trimLeft(" ${variable}", " $"));
        assertEquals("{variable}$", StringUtils.trimLeft("{variable}$", "="));
        assertEquals("${variable}", StringUtils.trimLeft("${variable}", "<"));
    }
}