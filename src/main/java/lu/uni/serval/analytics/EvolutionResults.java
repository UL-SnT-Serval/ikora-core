package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.UnorderedPair;

import java.util.*;

public class EvolutionResults {
    private SortedSet<Project> projects;

    private Map<Project, Map<Project, Set<Difference>>> differences;
    private Map<Project, Map<Project, Set<UnorderedPair<Differentiable>>>> keywords;
    private Map<Differentiable, Difference> keywordToDifference;
    private List<DifferentiableSequence> sequences;

    EvolutionResults(){
        projects = new TreeSet<>();
        differences = new LinkedHashMap<>();
        keywords = new LinkedHashMap<>();
        keywordToDifference = new LinkedHashMap<>();
        sequences = new ArrayList<>();
    }

    public void addDifference(Project project1, Project project2, Difference difference) {
        projects.add(project1);
        projects.add(project2);

        if(difference.getLeft() != null)
        {
            keywordToDifference.put(difference.getLeft(), difference);
        }
        if(difference.isEmpty()){
            return;
        }

        update(project1, project2, difference, differences);
        update(project1, project2, UnorderedPair.of(difference.getLeft(), difference.getRight()), keywords);
        updateSequence(difference);
    }

    public Set<Project> getComparedTo(Project project){
        return differences.getOrDefault(project,  new LinkedHashMap<>()).keySet();
    }

    public SortedSet<Project> getProjects(){
        return projects;
    }

    public Difference getDifference(Differentiable keyword){
        return keywordToDifference.getOrDefault(keyword, Difference.of(keyword, null));
    }

    public Set<Difference> getDifferences(Project project1, Project project2){
        return differences.getOrDefault(project1,  new LinkedHashMap<>()).get(project2);
    }

    public boolean containsElement(Project project1, Project project2, Differentiable keyword1, Differentiable keyword2){
        Map<Project, Set<UnorderedPair<Differentiable>>> comparedTo = keywords.get(project1);

        if(comparedTo == null){
            return false;
        }

        Set<UnorderedPair<Differentiable>> list = comparedTo.get(project2);

        if(list == null){
            return false;
        }

        return list.contains(UnorderedPair.of(keyword1, keyword2));
    }

    private <T> void update(Project left, Project right, T value, Map<Project, Map<Project, Set<T>>> container){
        Map<Project, Set<T>> comparedTo = container.getOrDefault(left, new LinkedHashMap<>());
        Set<T> list = comparedTo.getOrDefault(right, new HashSet<>());

        list.add(value);

        comparedTo.put(right, list);
        container.put(left, comparedTo);
    }

    private void updateSequence(Difference difference){
        for(DifferentiableSequence sequence: sequences){
            if(sequence.add(difference)){
                break;
            }
        }

        DifferentiableSequence sequence = new DifferentiableSequence();
        sequence.add(difference);

        sequences.add(sequence);
    }
}
