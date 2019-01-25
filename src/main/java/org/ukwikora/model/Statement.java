package org.ukwikora.model;


import javax.annotation.Nonnull;
import java.util.Set;

public interface Statement extends Differentiable {
    void setFile(@Nonnull TestCaseFile file);
    TestCaseFile getFile();
    String getFileName();

    void setLineRange(@Nonnull LineRange range);
    LineRange getLineRange();
    int getLoc();

    long getEpoch();

    Value getNameAsArgument();

    void accept(StatementVisitor visitor);

    boolean matches(@Nonnull String name);

    Set<Statement> getDependencies();
    void addDependency(@Nonnull Statement dependency);
}
