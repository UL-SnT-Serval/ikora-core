package org.ukwikora.analytics;

import com.beust.jcommander.ParameterException;
import org.ukwikora.model.*;
import org.ukwikora.utils.StringUtils;

import java.util.*;

public class ProjectStatistics {
    enum Metric{
        Size, Connectivity, Sequence, Depth, BranchIndex
    }

    final private Project project;

    private Set<Statement> userKeywords;
    private Set<Statement> testCases;

    public ProjectStatistics(Project project){
        this.project = project;
        userKeywords = this.project.getStatements(UserKeyword.class);
        testCases = this.project.getStatements(TestCase.class);
    }

    public int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
    }

    public int getLoc(){
        return this.project.getLoc();
    }

    public <T extends KeywordDefinition> int getNumberKeywords(Class<T> type){
        Set<T> keywords = getKeywords(type);

        if(keywords != null){
            return keywords.size();
        }

        return 0;
    }

    public int getDocumentationLength() {
        int length = 0;

        Set<Statement> keywords = new HashSet<>();
        keywords.addAll(userKeywords);
        keywords.addAll(testCases);

        for (Statement keyword: keywords){
            length += StringUtils.countLines(((KeywordDefinition)keyword).getDocumentation());
        }

        return length;
    }

    public <T extends KeywordDefinition> Map<Integer, Integer> getSizeDistribution(Class<T> type){
        return getDistribution(type, Metric.Size);
    }

    public <T extends KeywordDefinition> Map<Integer, Integer> getConnectivityDistribution(Class<T> type){
        return getDistribution(type, Metric.Connectivity);
    }

    public <T extends KeywordDefinition> Map<Integer, Integer> getSequenceDistribution(Class<T> type){
        return getDistribution(type, Metric.Sequence);
    }

    public <T extends KeywordDefinition> Map<Integer, Integer> getDepthDistribution(Class<T> type){
        return getDistribution(type, Metric.Depth);
    }

    <T extends KeywordDefinition> Map<Integer, Integer> getBranchIndex(Class<T> type){
        return getDistribution(type, Metric.BranchIndex);
    }

    private <T extends KeywordDefinition> Map<Integer, Integer> getDistribution(Class<T> type, Metric metric){
        Map<Integer, Integer> distribution = new TreeMap<>();

        for(KeywordDefinition keyword: getKeywords(type)){
            int value = -1;

            switch (metric){
                case Size: value = getSize(keyword); break;
                case Connectivity: value = getConnectivity(keyword); break;
                case Sequence: value = keyword.getMaxSequenceSize(); break;
                case Depth: value = getLevel(keyword); break;
                case BranchIndex: value = keyword.getBranchIndex(); break;
            }

            distribution.merge(value, 1, Integer::sum);
        }

        return distribution;
    }

    private <T extends Statement> Set<T> getKeywords(Class<T> type){
        if(type == UserKeyword.class){
            return (Set<T>)userKeywords;
        }
        else if(type == TestCase.class){
            return (Set<T>)testCases;
        }

        throw new ParameterException("Unhandled type " + type.getName());
    }

    public static int getConnectivity(Statement statement){
        ConnectivityVisitor visitor = new ConnectivityVisitor();
        statement.accept(visitor, new PathMemory());

        return visitor.getConnectivity();
    }

    public static int getSize(Statement statement){
        SizeVisitor visitor = new SizeVisitor();
        statement.accept(visitor, new PathMemory());

        return visitor.getSize();
    }

    public static int getLevel(Statement statement){
        LevelVisitor visitor = new LevelVisitor();
        statement.accept(visitor, new LevelMemory());

        return visitor.getLevel();
    }
}
