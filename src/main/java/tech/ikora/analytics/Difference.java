package tech.ikora.analytics;

import tech.ikora.model.Differentiable;
import tech.ikora.utils.LevenshteinDistance;
import tech.ikora.model.KeywordDefinition;

import java.util.*;

public class Difference implements Differentiable {
    private final Differentiable left;
    private final Differentiable right;
    private final List<Edit> edits;

    private Difference(Differentiable left, Differentiable right){
        this.left = left;
        this.right = right;

        this.edits = new ArrayList<>();
    }

    public boolean isEmpty(){
        return edits.isEmpty();
    }

    public boolean isEmpty(Set<Edit.Type> ignore){
        if(edits.isEmpty()){
            return true;
        }

        if(ignore.isEmpty()){
            return isEmpty();
        }

        for(Edit edit : edits){
            if (!ignore.contains(edit.getType())){
                return false;
            }
        }

        return true;
    }

    public Differentiable getLeft(){
        return left;
    }

    public Differentiable getRight(){
        return right;
    }

    public Differentiable getValue(){
        if(left != null){
            return left;
        }

        return right;
    }

    public List<Edit> getEdits() {
        return edits;
    }

    public static Difference of(Differentiable before, Differentiable after) {
        Difference difference = new Difference(before, after);

        if(before == after){
            return difference;
        }

        if(before == null){
            difference.edits.add(Edit.addElement(after.getClass(), after));
            return difference;
        }

        if(after == null){
            difference.edits.add(Edit.removeElement(before.getClass(), before));
            return difference;
        }

        difference.edits.addAll(before.differences(after));

        return difference;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(other == null){
            return false;
        }

        if(this.getClass() != other.getClass()){
            return false;
        }

        Difference difference = (Difference)other;

        return this.left == difference.left
                && this.right == difference.right;
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = getNodeHash(hash, left);
        hash = getNodeHash(hash, right);

        return hash;
    }

    private int getNodeHash(int hash, Differentiable element){
        if(element == null){
            hash = 31 * hash;
        }
        else if(element instanceof KeywordDefinition){
            KeywordDefinition keyword = (KeywordDefinition)element;

            hash = 31 * hash + (keyword.getSourceFile() == null ? 0 : keyword.getSourceFile().hashCode());
            hash = 31 * hash + (keyword.getNameToken() == null ? 0 : keyword.getNameToken().hashCode());
        }
        else{
            hash = element.hashCode();
        }

        return hash;
    }

    @Override
    public double distance(Differentiable other) {
        if(other == null){
            return 1.0;
        }

        if(other == this){
            return 0.0;
        }

        if(other.getClass() != this.getClass()){
            return 1.0;
        }

        if(((Difference)other).getValue().getClass() != this.getValue().getClass()){
            return 1.0;
        }

        return LevenshteinDistance.index(this.edits, ((Difference)other).edits);
    }

    @Override
    public List<Edit> differences(Differentiable other) {
        return Collections.emptyList();
    }

    public Class<?> getType() {
        return this.getValue().getClass();
    }
}
