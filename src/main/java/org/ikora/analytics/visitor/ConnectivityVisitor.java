package org.ikora.analytics.visitor;

import org.ikora.model.*;

public class ConnectivityVisitor extends DependencyVisitor {
    private int connectivity;

    public ConnectivityVisitor() {
        this.connectivity = 0;
    }

    public int getConnectivity(){
        return connectivity;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        // connectivity is always 0
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        connectivity += keyword.getDependencies().isEmpty() ? 0 : 1;
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        connectivity += keyword.getDependencies().isEmpty() ? 0 : 1;
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }
}
