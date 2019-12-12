package org.ikora.analytics.visitor;

import org.ikora.model.*;

import java.util.HashSet;
import java.util.Set;

public class FindSuiteVisitor extends DependencyVisitor {
    private Set<String> suites;

    public FindSuiteVisitor() {
        suites = new HashSet<>();
    }

    public Set<String> getSuites() {
        return suites;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        if(testCase != null){
            suites.add(testCase.getFileName());
        }
    }
}
