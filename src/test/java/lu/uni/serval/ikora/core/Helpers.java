package lu.uni.serval.ikora.core;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.builder.LineReader;
import lu.uni.serval.ikora.core.model.Project;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class Helpers {
    public static double epsilon = 0.0001;

    public static Project compileProject(String resourcesPath, boolean resolve) {
        File projectFolder = null;
        try {
            projectFolder = lu.uni.serval.ikora.core.utils.FileUtils.getResourceFile(resourcesPath);
        } catch (Exception e) {
            fail(String.format("Failed to load '%s': %s", resourcesPath, e.getMessage()));
        }

        final BuildResult result = Builder.build(projectFolder, getConfiguration(), resolve);
        Assertions.assertEquals(1, result.getProjects().size());

        return result.getProjects().iterator().next();
    }

    public static File getNewTmpFolder(String name){
        String tmpPath = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpPath);
        File directory = new File(tmpDir, name);
        deleteDirectory(directory);

        assertFalse(directory.exists());

        return directory;
    }

    public static void deleteDirectory(File directory){
        if(directory.exists()){
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                fail("Failed to clean " + directory.getAbsolutePath());
            }
        }
    }

    public static BuildConfiguration getConfiguration(){
        final BuildConfiguration configuration = new BuildConfiguration();
        configuration.setExtensions(Collections.singletonList("robot"));
        configuration.setIgnorePath(Collections.emptyList());

        return configuration;
    }

    public static LineReader lineReader(String text) throws IOException {
        LineReader lineReader = new LineReader(text);
        lineReader.readLine();

        return lineReader;
    }

    public static LineReader lineReader(String... tokens) throws IOException {
        String line = String.join("\t", tokens);
        return lineReader(line);
    }
}
