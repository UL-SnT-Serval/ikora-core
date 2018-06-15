package lu.uni.serval.robotframework.model;

import java.io.File;
import java.util.*;

public class Project {
    private List<TestCaseFile> testCaseFiles;
    private Map<File, TestCaseFile> files;

    public Project(){
        testCaseFiles = new ArrayList<>();
        files = new HashMap<>();
    }

    public boolean hasFile(File file){
        return files.containsKey(file);
    }

    public List<TestCaseFile> getTestCaseFiles(){
        return testCaseFiles;
    }

    public TestCaseFile getTestCaseFile(File file) {
        return files.get(file);
    }

    public TestCaseFile getFile(String path){
        return files.get(path);
    }

    public Map<File, TestCaseFile> getFiles(){
        return files;
    }

    public void addFile(File file){
        if(hasFile(file)){
            return;
        }

        files.put(file, null);
    }

    public void addTestCaseFile(TestCaseFile testCaseFile){
        testCaseFiles.add(testCaseFile);

        files.put(testCaseFile.getFile(), testCaseFile);

        updateFiles(testCaseFile.getSettings());
    }

    private void updateFiles(Settings settings){
        for(Resources resources: settings.getResources()){
            addFile(resources.getFile());
        }
    }
}
