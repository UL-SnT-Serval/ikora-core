package lu.uni.serval.analytics;

class CloneIndex {
    public enum Ignore{
        Subtree(-1.0),
        OneStep(-2.0);

        private final double ignore;

        Ignore(double ignore) {
            this.ignore = ignore;
        }

        public double getValue(){
            return ignore;
        }
    }
    
    private static double treeThreshold = 0.9;
    private static double keywordThreshold = 0.9;

    private final double treeRatio;
    private final double keywordRatio;
    private final double semanticRatio;

    public CloneIndex(double keywordRatio, double treeRatio, double semanticRatio) {
        this.treeRatio = treeRatio;
        this.keywordRatio = keywordRatio;
        this.semanticRatio = semanticRatio;
    }

    public boolean isClone(){
        return treeRatio > treeThreshold;
    }

    public boolean isHomonym(){
        return !isClone() && keywordRatio > keywordThreshold;
    }

    public boolean isSynonym(){
        return isClone() && keywordRatio < keywordThreshold;
    }

    public boolean isSame(){
        return isClone() && keywordRatio > keywordThreshold;
    }

    public double getTreeRatio(){
        return treeRatio;
    }

    public double getKeywordRatio(){
        return keywordRatio;
    }

    public double getSemanticRatio(){
        return semanticRatio;
    }

    public static double getKeywordThreshold(){
        return CloneIndex.keywordThreshold;
    }

    public static double getTreeThreshold(){
        return CloneIndex.treeThreshold;
    }

    public static void setKeywordThreshold(double keywordThreshold) {
        CloneIndex.keywordThreshold = keywordThreshold;
    }

    public static void setTreeThreshold(double treeThreshold){
        CloneIndex.treeThreshold = treeThreshold;
    }
}
