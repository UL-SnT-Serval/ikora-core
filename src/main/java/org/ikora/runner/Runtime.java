package org.ikora.runner;

import org.ikora.model.*;
import org.ikora.report.Report;
import org.ikora.report.ReportBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Runtime {
    private Scope scope;
    private LibraryResources libraries;
    private Project project;
    private ReportBuilder reportBuilder;

    public Runtime(Project project, Scope scope){
        this.project = project;
        this.scope = scope;
        this.reportBuilder = new ReportBuilder();
    }

    public void setLibraries(LibraryResources libraries) {
        this.libraries = libraries;
    }

    public Set<Variable> findLibraryVariable(String name){
        return Collections.singleton(this.libraries.findVariable(name));
    }

    public List<SourceFile> getSourceFiles(){
        return project.getSourceFiles();
    }

    public Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name){
        return scope.findInScope(testCases, suites, name);
    }

    public Keyword findKeyword(String library, String name) throws InstantiationException, IllegalAccessException{
        if(library == null){
            return null;
        }

        return libraries.findKeyword(library, name);
    }

    public void addToGlobalScope(Variable variable){
        this.scope.addToGlobal(variable);
    }

    public void addToSuiteScope(String suite, Variable variable){
        this.scope.addToSuite(suite, variable);
    }

    public void addToTestScope(TestCase testCase, Variable variable){
        this.scope.addToTest(testCase, variable);
    }

    public void addToKeywordScope(Keyword keyword, Variable variable) {
        this.scope.addToKeyword(keyword, variable);
    }

    public void addDynamicLibrary(KeywordDefinition keyword, List<Value> parameters){
        this.scope.addDynamicLibrary(keyword, parameters);
    }

    public void reset(){
        this.scope.reset();
        this.reportBuilder.reset();
    }

    public void enterSuite(Suite suite) throws Exception {
        this.scope.enterSuite(suite);
        this.reportBuilder.enterSuite(suite);
    }

    public void enterKeyword(Keyword keyword) throws Exception {
        this.scope.enterKeyword(keyword);
        this.reportBuilder.enterKeyword(keyword);
    }

    public void exitSuite(Suite suite) {
        this.scope.exitSuite(suite);
        this.reportBuilder.exitSuite(suite);
    }

    public void exitKeyword(Keyword keyword){
        this.scope.exitKeyword(keyword);
        this.reportBuilder.exitKeyword(keyword);
    }

    public void finish() {
        this.reportBuilder.finish();
    }

    Optional<Report> getReport(){
        return reportBuilder.getReport();
    }

    public KeywordDefinition getTestCase() {
        return scope.getTestCase();
    }

    public List<Value> getReturnValues() {
        return scope.getReturnValues();
    }
}