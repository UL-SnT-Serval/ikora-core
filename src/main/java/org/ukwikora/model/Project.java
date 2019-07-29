package org.ukwikora.model;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Project implements Comparable<Project> {
    private List<Suite> suites;
    private Map<String, TestCaseFile> files;
    private Set<Project> dependencies;

    private File rootFolder;
    private String gitUrl;
    private String commitId;
    private LocalDateTime dateTime;
    private int loc;

    public Project(String file){
        rootFolder = new File(file.trim());
        suites = new ArrayList<>();
        files = new HashMap<>();
        dependencies = new HashSet<>();
        loc = 0;
    }

    void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getName() {
        return rootFolder.getName();
    }

    public File getRootFolder() {
        return rootFolder;
    }

    public List<Suite> getSuites(){
        return suites;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<TestCaseFile> getTestCaseFiles(){
        return new ArrayList<>(files.values());
    }

    public TestCaseFile getTestCaseFile(String name) {
        return files.get(name);
    }

    public Map<String, TestCaseFile> getFiles(){
        return files;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public List<TestCase> getTestCases(){
        List<TestCase> testCases = new ArrayList<>();

        for(TestCaseFile file: files.values()){
            testCases.addAll(file.getTestCases());
        }

        return testCases;
    }

    public Set<UserKeyword> getUserKeywords(){
        Set<UserKeyword> userKeywords = new HashSet<>();

        for(TestCaseFile file: files.values()){
            userKeywords.addAll(file.getUserKeywords());
        }

        return userKeywords;
    }

    public Set<UserKeyword> findUserKeyword(String name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(TestCaseFile file: files.values()){
            userKeywordsFound.addAll(file.findUserKeyword(name));
        }

        return userKeywordsFound;
    }

    public Set<UserKeyword> findUserKeyword(String library, String name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(TestCaseFile file: files.values()){
            userKeywordsFound.addAll(file.findUserKeyword(library, name));
        }

        return userKeywordsFound;
    }

    public Set<Variable> getVariables(){
        Set<Variable> variables = new HashSet<>();

        for(TestCaseFile file: files.values()){
            variables.addAll(file.getVariables());
        }

        return variables;
    }

    public <T> Set<T> getStatements(Class<T> type) {
        if(type == TestCase.class){
            return (Set<T>)new HashSet<>(getTestCases());
        }
        else if(type == UserKeyword.class) {
            return (Set<T>)new HashSet<>(getUserKeywords());
        }
        else if(type == Variable.class) {
            return (Set<T>)new HashSet<>(getVariables());
        }

        return Collections.emptySet();
    }

    public Set<Resources> getExternalResources() {
        Set<Resources> externalResources = new HashSet<>();

        for(TestCaseFile file: files.values()){
            externalResources.addAll(file.getSettings().getExternalResources());
        }

        return externalResources;
    }

    public long getEpoch() {
        ZoneId zoneId = ZoneId.systemDefault();
        return this.getDateTime().atZone(zoneId).toEpochSecond();
    }

    public int getLoc() {
        return loc;
    }

    public int getDeadLoc() {
        int deadLoc = 0;

        for(TestCaseFile file: files.values()){
            deadLoc += file.getDeadLoc();
        }

        return deadLoc;
    }

    public Set<Project> getDependencies() {
        return this.dependencies;
    }

    public boolean isDependency(Project project) {
        return this.dependencies.contains(project);
    }

    public void addFile(File file){
        String key = generateFileName(file);

        if(files.containsKey(key)){
            return;
        }

        files.put(key, null);
    }

    public void addTestCaseFile(TestCaseFile testCaseFile){
        if(testCaseFile == null){
            return;
        }

        files.put(testCaseFile.getName(), testCaseFile);
        updateSuites(testCaseFile);
        updateFiles(testCaseFile.getSettings());

        this.loc += testCaseFile.getLoc();
    }

    private void updateSuites(TestCaseFile testCaseFile){
        if(testCaseFile.getTestCases().isEmpty()){
            return;
        }

        String name = SuiteFactory.computeName(testCaseFile, true);
        Optional<Suite> suite = suites.stream().filter(s -> s.getName().equals(name)).findAny();

        if(suite.isPresent()){
            suite.get().addTestCaseFile(testCaseFile);
        }
        else {
            suites.add(SuiteFactory.create(testCaseFile));
        }
    }

    private void updateFiles(Settings settings){
        for(Resources resources: settings.getResources()){
            addFile(resources.getFile());
        }
    }

    public void addDependency(Project dependency) {
        if(dependency != null){
            dependencies.add(dependency);
        }
    }

    public String generateFileName(File file) {
        File rootFolder = this.getRootFolder();

        if(rootFolder.isFile()){
            rootFolder = rootFolder.getParentFile();
        }

        Path base = Paths.get(rootFolder.getAbsolutePath().trim());

        Path path = Paths.get(file.getAbsolutePath().trim()).normalize();

        return base.relativize(path).toString();
    }

    @Override
    public int compareTo(@Nonnull Project other) {
        if(dateTime == other.dateTime){
            return 0;
        }

        return dateTime.isBefore(other.dateTime) ? -1 : 1;
    }
}
