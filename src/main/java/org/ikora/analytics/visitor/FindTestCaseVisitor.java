package org.ikora.analytics.visitor;

import org.ikora.model.*;

import java.util.HashSet;
import java.util.Set;

public class FindTestCaseVisitor extends DependencyVisitor {
    private Set<TestCase> testCases;

    public FindTestCaseVisitor(){
        testCases = new HashSet<>();
    }

    public Set<TestCase> getTestCases(){
        return testCases;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        testCases.add(testCase);
    }
}
