package lu.uni.serval.analytics;

import lu.uni.serval.utils.CompareCache;
import lu.uni.serval.utils.tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CloneResults {
    public enum CloneType{
        None, Same, Synonym, Homonym
    }

    private Map<CloneType, CompareCache<TreeNode, CloneIndex>> results;

    public CloneResults(){
        results = new HashMap<CloneType, CompareCache<TreeNode, CloneIndex>>();

        results.put(CloneType.Same, new CompareCache<TreeNode, CloneIndex>());
        results.put(CloneType.Synonym, new CompareCache<TreeNode, CloneIndex>());
        results.put(CloneType.Homonym, new CompareCache<TreeNode, CloneIndex>());
    }

    public CompareCache<TreeNode, CloneIndex> getSame(){
        return results.get(CloneType.Same);
    }

    public CompareCache<TreeNode, CloneIndex> getSynonym(){
        return results.get(CloneType.Synonym);
    }

    public CompareCache<TreeNode, CloneIndex> getHomonym(){
        return results.get(CloneType.Homonym);
    }

    public CompareCache<TreeNode, CloneIndex> getByType(CloneType type){
        return results.get(type);
    }

    public void update(CloneIndex cloneIndex, TreeNode tree1, TreeNode tree2) {
        CloneType type = CloneType.None;

        if(cloneIndex.isSame()){
            type = CloneType.Same;
        } else if(cloneIndex.isHomonym()){
            type = CloneType.Homonym;
        } else if(cloneIndex.isSynonym()){
            type = CloneType.Synonym;
        }

        if(type != CloneType.None){
            update(results.get(type), cloneIndex, tree1, tree2);
        }
    }

    private void update(CompareCache<TreeNode, CloneIndex> clones, CloneIndex cloneIndex,
                        TreeNode tree1, TreeNode tree2){
        if(clones == null){
            return;
        }

        clones.set(tree1, tree2, cloneIndex);
    }
}