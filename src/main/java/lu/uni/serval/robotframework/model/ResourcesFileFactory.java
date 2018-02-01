package lu.uni.serval.robotframework.model;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.File;

public class ResourcesFileFactory extends TestCaseFileFactory {

    public ResourcesFileFactory() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("from robot.api import ResourceFile");

        super.testCaseFileClass = interpreter.get("ResourceFile");
    }

    @Override
    public TestCaseFile create(String filePath) {
        this.file = filePath;
        PyObject testCaseFileObject = testCaseFileClass.__call__(new PyString(this.file));
        testCaseFileObject.__findattr__("populate").__call__();

        String directory = getStringValue(testCaseFileObject, "directory");
        String name = getStringValue(testCaseFileObject, "name");
        Settings settings = createSettingsTable(testCaseFileObject.__findattr__("setting_table"));
        KeywordTable keywordTable = createKeywordTable(testCaseFileObject.__findattr__("keyword_table"));
        VariableTable variableTable = createVariableTable(testCaseFileObject.__findattr__("variable_table"));

        loadResources(settings);

        return new TestCaseFile(directory, name, settings, null, keywordTable, variableTable);
    }
}