package org.ukwikora.analytics;

import org.ukwikora.model.KeywordDefinition;
import org.ukwikora.model.Sequence;
import org.ukwikora.model.Node;

import java.util.Set;

public class KeywordStatistics {
    public static int getConnectivity(Node node){
        ConnectivityVisitor visitor = new ConnectivityVisitor();
        node.accept(visitor, new PathMemory());

        return visitor.getConnectivity();
    }

    public static int getSize(Node node){
        SizeVisitor visitor = new SizeVisitor();
        node.accept(visitor, new PathMemory());

        return visitor.getSize();
    }

    public static int getLevel(Node node){
        LevelVisitor visitor = new LevelVisitor();
        node.accept(visitor, new LevelMemory());

        return visitor.getLevel();
    }

    public static int getSequenceSize(Node node){
        SequenceVisitor visitor = new SequenceVisitor();
        node.accept(visitor, new PathMemory());

        return visitor.getSequenceSize();
    }

    public static Sequence getSequence(Node node){
        SequenceVisitor visitor = new SequenceVisitor();
        node.accept(visitor, new PathMemory());

        return visitor.getSequence();
    }

    public static Set<KeywordDefinition> getDependencies(Node node, Set<KeywordDefinition> filter){
        DependencyCheckerVisitor visitor = new DependencyCheckerVisitor(filter);
        node.accept(visitor, new PathMemory());

        return visitor.getDependencies();
    }
}
