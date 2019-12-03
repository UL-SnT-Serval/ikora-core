package org.ukwikora.analytics;

import org.ukwikora.exception.InvalidTypeException;
import org.ukwikora.model.*;
import org.ukwikora.utils.StringUtils;

import java.util.*;

public class ProjectStatistics {
    enum Metric{
        Size, Connectivity, Sequence, Level, BranchIndex
    }

    final private Project project;

    private Set<UserKeyword> userKeywords;
    private Set<TestCase> testCases;

    public ProjectStatistics(Project project){
        this.project = project;
        userKeywords = this.project.getNodes(UserKeyword.class);
        testCases = this.project.getNodes(TestCase.class);
    }

    public int getNumberFiles(){
        return this.project.getSourceFiles().size();
    }

    public int getLoc(){
        return this.project.getLoc();
    }

    public <T extends KeywordDefinition> int getNumberKeywords(Class<T> type) throws InvalidTypeException {
        Set<T> keywords = getNodes(type);

        if(keywords != null){
            return keywords.size();
        }

        return 0;
    }

    public int getDocumentationLength() {
        int length = 0;

        Set<Node> keywords = new HashSet<>();
        keywords.addAll(userKeywords);
        keywords.addAll(testCases);

        for (Node keyword: keywords){
            length += StringUtils.countLines(((KeywordDefinition)keyword).getDocumentation());
        }

        return length;
    }

    public <T extends Node> Map<Integer, Integer> getSizeDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.Size);
    }

    public <T extends Node> Map<Integer, Integer> getConnectivityDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.Connectivity);
    }

    public <T extends Node> Map<Integer, Integer> getSequenceDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.Sequence);
    }

    public <T extends Node> Map<Integer, Integer> getLevelDistribution(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.Level);
    }

    public <T extends Node> Map<String, Integer> getDeadCodeDistribution(Class<T> type) throws InvalidTypeException {
        Map<String, Integer> deadCode = new HashMap<>(2);

        for(Node node : getNodes(type)){
            deadCode.merge(node.isDeadCode() ? "Dead" : "Executed", node.getLoc(), Integer::sum);
        }

        return deadCode;
    }

    <T extends Node> Map<Integer, Integer> getBranchIndex(Class<T> type) throws InvalidTypeException {
        return getDistribution(type, Metric.BranchIndex);
    }

    private <T extends Node> Map<Integer, Integer> getDistribution(Class<T> type, Metric metric) throws InvalidTypeException {
        Map<Integer, Integer> distribution = new TreeMap<>();

        for(Node node : getNodes(type)){
            int value = -1;

            switch (metric){
                case Size: value = KeywordStatistics.getSize(node); break;
                case Connectivity: value = KeywordStatistics.getConnectivity(node); break;
                case Sequence: value = KeywordStatistics.getSequenceSize(node); break;
                case Level: value = KeywordStatistics.getLevel(node); break;
                //case BranchIndex: value = keyword.getBranchIndex(); break;
            }

            distribution.merge(value, 1, Integer::sum);
        }

        return distribution;
    }

    private <T extends Node> Set<T> getNodes(Class<T> type) throws InvalidTypeException {
        if(type == UserKeyword.class){
            return (Set<T>)userKeywords;
        }
        else if(type == TestCase.class){
            return (Set<T>)testCases;
        }

        throw new InvalidTypeException("Unhandled type " + type.getName());
    }
}
