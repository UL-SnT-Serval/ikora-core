package tech.ikora.model;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Project implements Comparable<Project> {
    private List<Suite> suites;
    private Map<String, SourceFile> files;
    private Set<Project> dependencies;

    private Source rootFolder;
    private Date date;
    private int loc;

    public Project(Source source){
        rootFolder = source;
        suites = new ArrayList<>();
        files = new HashMap<>();
        dependencies = new HashSet<>();
        loc = 0;
    }

    public String getName() {
        return rootFolder.getName();
    }

    public Source getRootFolder() {
        return rootFolder;
    }

    public List<Suite> getSuites(){
        return suites;
    }

    public Date getDate() {
        return date;
    }

    public List<SourceFile> getSourceFiles(){
        return new ArrayList<>(files.values());
    }

    public Optional<SourceFile> getSourceFile(String name) {
        return Optional.ofNullable(files.get(name));
    }

    public Optional<SourceFile> getSourceFile(URI uri){
        String name = generateFileName(new Source(new File(uri.getPath())));
        return getSourceFile(name);
    }

    public Map<String, SourceFile> getFiles(){
        return files;
    }

    public List<TestCase> getTestCases(){
        List<TestCase> testCases = new ArrayList<>();

        for(SourceFile file: files.values()){
            testCases.addAll(file.getTestCases());
        }

        return testCases;
    }

    public Set<UserKeyword> getUserKeywords(){
        Set<UserKeyword> userKeywords = new HashSet<>();

        for(SourceFile file: files.values()){
            userKeywords.addAll(file.getUserKeywords());
        }

        return userKeywords;
    }

    public Set<TestCase> findTestCase(String library, String name) {
        Set<TestCase> testCasesFound = new HashSet<>();

        for(SourceFile file: files.values()){
            testCasesFound.addAll(file.findTestCase(library, Token.fromString(name)));
        }

        return testCasesFound;
    }

    public Set<UserKeyword> findUserKeyword(Token name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(SourceFile file: files.values()){
            userKeywordsFound.addAll(file.findUserKeyword(name));
        }

        return userKeywordsFound;
    }

    public Set<UserKeyword> findUserKeyword(String library, String name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(SourceFile file: files.values()){
            userKeywordsFound.addAll(file.findUserKeyword(library, Token.fromString(name)));
        }

        return userKeywordsFound;
    }

    public Set<VariableAssignment> getVariables(){
        Set<VariableAssignment> variables = new HashSet<>();

        for(SourceFile file: files.values()){
            variables.addAll(file.getVariables());
        }

        return variables;
    }

    public <T extends SourceNode> Set<T> getNodes(Class<T> type) {
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

        for(SourceFile file: files.values()){
            externalResources.addAll(file.getSettings().getExternalResources());
        }

        return externalResources;
    }

    public long getEpoch() {
        return this.getDate().toInstant().toEpochMilli();
    }

    public int getLoc() {
        return loc;
    }

    public int getDeadLoc() {
        int deadLoc = 0;

        for(SourceFile file: files.values()){
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

    public void addFile(Source source){
        String key = generateFileName(source);

        if(files.containsKey(key)){
            return;
        }

        files.put(key, null);
    }

    public void addSourceFile(SourceFile sourceFile){
        if(sourceFile == null){
            return;
        }

        files.put(sourceFile.getName(), sourceFile);
        updateSuites(sourceFile);
        updateFiles(sourceFile.getSettings());

        this.loc += sourceFile.getLinesOfCode();
    }

    private void updateSuites(SourceFile sourceFile){
        if(sourceFile.getTestCases().isEmpty()){
            return;
        }

        if(sourceFile.getSource().isInMemory()){
            return;
        }

        String name = SuiteFactory.computeName(sourceFile, true);
        Optional<Suite> suite = suites.stream().filter(s -> s.getName().equals(name)).findAny();

        if(suite.isPresent()){
            suite.get().addSourceFile(sourceFile);
        }
        else {
            suites.add(SuiteFactory.create(sourceFile));
        }
    }

    private void updateFiles(Settings settings){
        for(Resources resources: settings.getInternalResources()){
            addFile(new Source(resources.getFile()));
        }
    }

    public void addDependency(Project dependency) {
        if(dependency != null){
            dependencies.add(dependency);
        }
    }

    public String generateFileName(Source source) {
        if(source.isInMemory()){
            return source.getName();
        }

        File rootFolder = this.getRootFolder().asFile();

        if(rootFolder.isFile()){
            rootFolder = rootFolder.getParentFile();
        }

        Path base = Paths.get(rootFolder.getAbsolutePath().trim());

        Path path = Paths.get(source.getAbsolutePath().trim()).normalize();

        return base.relativize(path).toString();
    }

    @Override
    public int compareTo(Project other) {
        return date.compareTo(other.date);
    }
}
