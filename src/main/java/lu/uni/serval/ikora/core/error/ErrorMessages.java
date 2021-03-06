package lu.uni.serval.ikora.core.error;

public class ErrorMessages {
    private ErrorMessages() {}

    public static final String EMPTY_TOKEN_NOT_EXPECTED = "Empty token not expected";
    public static final String EMPTY_TOKEN_SHOULD_BE_KEYWORD = "Empty token should be a keyword call";
    public static final String FAILED_TO_CREATE_DEPENDENCY = "Failed to create dependency";
    public static final String FAILED_TO_ADD_STEP_TO_TEST_CASE = "Failed to add step to test case";
    public static final String FAILED_TO_ADD_STEP_TO_KEYWORD = "Failed to add step to keyword";
    public static final String SHOULD_HANDLE_STATIC_IMPORT = "Should handle Static import at this point";
    public static final String FOUND_MULTIPLE_MATCHES = "Found multiple matches to resolve symbol";
    public static final String FOUND_NO_MATCH = "Found no match to resolve symbol";
    public static final String FAILED_TO_LOAD_LIBRARY_KEYWORD = "Failed to load library keyword";
    public static final String FAILED_TO_PARSE_FORLOOP = "Failed to parser for loop";
    public static final String FAILED_TO_LOCATE_ITERATOR_IN_FOR_LOOP = "Failed to locate iterator when parsing for loop";
    public static final String FAILED_TO_CREATE_ITERATOR_IN_FOR_LOOP = "Failed to create iterator when parsing for loop";
    public static final String FAILED_TO_PARSE_TIMEOUT = "Failed to parse timeOut";
    public static final String FAILED_TO_PARSE_SETUP = "Failed to parse setup";
    public static final String FAILED_TO_PARSE_TEARDOWN = "Failed to parse teardown";
    public static final String FAILED_TO_PARSE_TEMPLATE = "Failed to parse template";
    public static final String FAILED_TO_PARSE_METADATA = "Failed to parse metadata";
    public static final String TOO_MANY_METADATA_ARGUMENTS = "Too many metadata arguments";
    public static final String FAILED_TO_LINK_TEMPLATE = "Failed to link template";
    public static final String FAILED_TO_PARSE_PARAMETER = "Failed to parse parameter";
    public static final String ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND = "Assignment should have left hand operand";
    public static final String RETURN_VALUE_SHOULD_BE_A_VARIABLE = "Return value should be a variable";
}
