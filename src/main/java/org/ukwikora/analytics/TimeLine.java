package org.ukwikora.analytics;

import org.ukwikora.model.Project;
import org.ukwikora.model.TestCaseFile;
import org.ukwikora.utils.LevenshteinDistance;
import org.ukwikora.model.Element;
import org.ukwikora.model.KeywordDefinition;

import java.util.*;

public class TimeLine implements StatusResults.Differentiable, Iterable<Difference> {

    private List<Difference> sequence;
    private LinkedHashSet<StatusResults.Differentiable> items;
    private List<Action> actions;
    private StatusResults.Differentiable last;
    private StatusResults.Differentiable lastValid;

    TimeLine() {
        sequence = new ArrayList<>();
        items = new LinkedHashSet<>();
        actions = new ArrayList<>();
        last = null;
        lastValid = null;
    }

    public boolean add(Difference difference){
        if(difference == null){
            return false;
        }

        if(this.sequence.size() > 0 && this.last != difference.getLeft()){
            return false;
        }

        this.sequence.add(difference);
        addElement(difference);
        addActions(difference);

        this.last = difference.getRight();
        this.lastValid = difference.getValue();

        return true;
    }

    private void addElement(Difference difference) {
        if(difference.getLeft() != null){
            items.add(difference.getLeft());
        }

        if(difference.getRight() != null){
            items.add(difference.getRight());
        }
    }

    private void addActions(Difference difference){
        for(Action action: difference.getActions()){
            if(!isCreation(action) && !isDeletion(action)){
                this.actions.add(action);
            }
        }
    }

    @Override
    public Iterator<Difference> iterator() {
        return sequence.iterator();
    }

    public int size() {
        return sequence.size();
    }

    public Difference get(int index){
        return sequence.get(index);
    }

    public Class<?> getType() {
        if(lastValid == null){
            return null;
        }

        return lastValid.getClass();
    }

    public String getName(){
        if(lastValid == null){
            return "";
        }

        return lastValid.getName();
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public StatusResults.Differentiable getLastValid() {
        return lastValid;
    }

    public Difference.Clone getCloneType(TimeLine other) {
        String commit = findFirstCommonCommit(other);

        if(commit.isEmpty()){
            return Difference.Clone.None;
        }

        Element thisItem = (Element)this.findItemByCommit(commit);
        Element otherItem = (Element)other.findItemByCommit(commit);

        return CloneDetection.getCloneType(thisItem, otherItem);
    }

    public LinkedHashSet<StatusResults.Differentiable> getItems() {
        return items;
    }

    private String findFirstCommonCommit(TimeLine other) {
        List<String> thisCommits = getCommits();
        List<String> otherCommits = other.getCommits();

        thisCommits.retainAll(otherCommits);

        if(thisCommits.isEmpty()){
            return "";
        }

        return thisCommits.get(0);
    }

    private List<String> getCommits() {
        List<String> commits = new ArrayList<>();

        for(StatusResults.Differentiable item: items){
            String commit = getItemCommit(item);

            if(!commit.isEmpty()){
                commits.add(commit);
            }
        }

        return commits;
    }

    private String getItemCommit(StatusResults.Differentiable item) {
        if(!Element.class.isAssignableFrom(item.getClass())){
            return "";
        }

        Element element = (Element)item;

        TestCaseFile file = element.getFile();
        if(file == null){
            return "";
        }

        Project project = file.getProject();
        if(project == null){
            return "";
        }

        return project.getCommitId();
    }

    private StatusResults.Differentiable findItemByCommit(String commit){
        for(StatusResults.Differentiable item: items){
            if(getItemCommit(item).equals(commit)){
                return item;
            }
        }

        return null;
    }

    public boolean isKeywordDefinition(){
        if(lastValid == null){
            return false;
        }

        return KeywordDefinition.class.isAssignableFrom(lastValid.getClass());
    }

    public boolean hasChanged(){
        return !this.actions.isEmpty();
    }

    private boolean isCreation(Action action){
        return action.getType() == Action.Type.ADD_USER_KEYWORD
                || action.getType() == Action.Type.ADD_TEST_CASE
                || action.getType() == Action.Type.ADD_VARIABLE;
    }

    private boolean isDeletion(Action action){
        return action.getType() == Action.Type.REMOVE_USER_KEYWORD
                || action.getType() == Action.Type.REMOVE_TEST_CASE
                || action.getType() == Action.Type.REMOVE_VARIABLE;
    }

    @Override
    public double distance(StatusResults.Differentiable other) {
        if(other.getClass() != this.getClass()){
            return 1.0;
        }

        if(!((TimeLine)other).getType().equals(this.getType())){
            return 1.0;
        }

        return LevenshteinDistance.index(actions, ((TimeLine)other).actions);
    }

    @Override
    public List<Action> differences(StatusResults.Differentiable other) {
        List<Action> invalid = new ArrayList<>();
        invalid.add(Action.invalid(this, other));

        if(other.getClass() != this.getClass()){
            return invalid;
        }

        if(((TimeLine)other).lastValid.getClass() != this.lastValid.getClass()){
            return invalid;
        }

        return LevenshteinDistance.getDifferences(this.actions, ((TimeLine)other).actions);
    }
}