package lu.uni.serval.utils.tree;

import lu.uni.serval.utils.CompareCache;

import java.util.ArrayList;
import java.util.List;

public class TreeEditDistance implements TreeDistance {
    private final EditScore score;

    public TreeEditDistance(EditScore score) {
        if(score == null) {
            throw new NullPointerException();
        }

        this.score = score;
    }

    public double distance(TreeNode tree1, TreeNode tree2) {
        if (tree1 == null || tree2 == null) {
            throw new NullPointerException();
        }

        CompareCache<TreeNode, ScoreElement> memory = new CompareCache<>();
        List<EditAction> actions = new ArrayList<>();

        return distance(memory, actions, tree1, tree2);
    }

    public double index(TreeNode tree1, TreeNode tree2) {
        if(tree1.getSize() == 0 && tree2.getSize() == 0) {
            return 0;
        }

        double distance = distance(tree1, tree2);
        double size = score.size(tree1, tree2);

        return distance / size;
    }

    public List<EditAction> differences(TreeNode tree1, TreeNode tree2){
        if(tree1.getSize() == 0 && tree2.getSize() == 0){
            return new ArrayList<>();
        }

        CompareCache<TreeNode, ScoreElement> memory = new CompareCache<>();
        List<EditAction> actions = new ArrayList<>();

        distance(memory, actions, tree1, tree2);

        return actions;
    }

    private double distance(CompareCache<TreeNode, ScoreElement> memory, List<EditAction> actions, TreeNode tree1, TreeNode tree2) {
        if(memory.isCached(tree1, tree2)) {
            actions.addAll(memory.getScore(tree1, tree2).actions);
            return memory.getScore(tree1, tree2).score;
        }

        double score;
        EditOperation operation;
        List<EditAction> subtreeActions = new ArrayList<>();

        if (tree1 == null && tree2 == null) {
            return 0.0;
        }
        else if (tree1 == null) {
            score = calculateInsertScore(memory, subtreeActions, null, tree2);
            operation = EditOperation.Insert;
        }
        else if (tree2 == null) {
            score = calculateDeleteScore(memory, subtreeActions, tree1, null);
            operation = EditOperation.Delete;
        }
        else {
            List<EditAction> replaceActions = new ArrayList<>();
            List<EditAction> deleteActions = new ArrayList<>();
            List<EditAction> insertActions = new ArrayList<>();

            double replace = calculateReplaceScore(memory, replaceActions, tree1, tree2);
            double delete = calculateDeleteScore(memory, deleteActions, tree1, tree2);
            double insert = calculateInsertScore(memory, insertActions, tree1, tree2);

            if(replace < delete && replace < insert) {
                score = replace;
                operation = EditOperation.Replace;
                subtreeActions = replaceActions;
            }
            else if (delete < insert) {
                score = delete;
                operation = EditOperation.Delete;
                subtreeActions = deleteActions;
            }
            else {
                score = insert;
                operation = EditOperation.Insert;
                subtreeActions = insertActions;
            }
        }

        actions.addAll(subtreeActions);
        memory.set(tree1, tree2, new ScoreElement(score, operation, subtreeActions));
        return score;
    }

    private double calculateReplaceScore(CompareCache<TreeNode, ScoreElement> memory, List<EditAction> actions, TreeNode tree1, TreeNode tree2) {
        double s1 = distance(memory, actions, getInside(tree1), getInside(tree2));
        double s2 = distance(memory, actions,  getOutside(tree1), getOutside(tree2));
        double replaceScore = score.replace(tree1, tree2);

        if(replaceScore > 0){
            EditAction action = new EditAction(EditOperation.Replace, tree1, tree2);
            actions.add(action);
        }

        return s1 + s2 + replaceScore;
    }

    private double calculateInsertScore(CompareCache<TreeNode, ScoreElement> memory, List<EditAction> actions, TreeNode tree1, TreeNode tree2) {
        EditAction action = new EditAction(EditOperation.Insert, tree1, tree2);
        actions.add(action);

        TreeNode newTree = deleteHead(tree2);
        return distance(memory, actions, tree1, newTree) + score.insert(newTree);
    }

    private double calculateDeleteScore(CompareCache<TreeNode, ScoreElement> memory, List<EditAction> actions, TreeNode tree1, TreeNode tree2) {
        EditAction action = new EditAction(EditOperation.Delete, tree1, tree2);
        actions.add(action);

        TreeNode newTree = deleteHead(tree1);
        return distance(memory, actions, newTree, tree2) + score.delete(newTree);
    }

    private TreeNode getInside(TreeNode node) {
        return node.getFirstChild();
    }

    private TreeNode getOutside(TreeNode node) {
        while(!node.isRoot()){
            TreeNode sibling = node.getNextSibling();

            if(sibling != null) {
                return sibling;
            }

            node = node.getParent();
        }

        return null;
    }

    private TreeNode deleteHead(TreeNode node) {
         TreeNode child = node.getFirstChild();

         if(child == null){
             return getOutside(node);
         }

         return child;
    }
}
