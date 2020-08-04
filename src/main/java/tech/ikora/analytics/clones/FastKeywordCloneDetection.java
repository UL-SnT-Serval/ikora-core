package tech.ikora.analytics.clones;

import tech.ikora.model.*;

import java.util.*;

public class FastKeywordCloneDetection {
    private final Set<Project> projects;
    private final Clones<KeywordDefinition> clones;

    private FastKeywordCloneDetection(Project project){
        this.projects = Collections.singleton(project);
        this.clones = new Clones<>();
    }

    private FastKeywordCloneDetection(Projects projects){
        this.projects = projects.asSet();
        this.clones = new Clones<>();
    }

    public static Clones<KeywordDefinition> findClones(Projects projects){
        FastKeywordCloneDetection detection = new FastKeywordCloneDetection(projects);
        return detection.run();
    }

    public static Clones<KeywordDefinition> findClones(Project project){
        FastKeywordCloneDetection detection = new FastKeywordCloneDetection(project);
        return detection.run();
    }

    private Clones<KeywordDefinition> run(){
        List<CloneHash> nodes = new ArrayList<>();

        for(Project project: projects){
            for(KeywordDefinition testCase: project.getTestCases()){
                nodes.add(new CloneHash(testCase));
            }

            for(KeywordDefinition userKeyword: project.getUserKeywords()){
                nodes.add(new CloneHash(userKeyword));
            }
        }

        int size = nodes.size();

        for(int i = 0; i < size; ++i){
            CloneHash c1 = nodes.get(i);

            for(int j = i + 1; j < size; ++j){
                CloneHash c2 = nodes.get(j);
                clones.update(c1.keyword, c2.keyword, getCloneType(c1, c2));
            }
        }

        return clones;
    }

    private Clone.Type getCloneType(CloneHash c1, CloneHash c2){
        if(c1.type1 == c2.type1){
            return Clone.Type.TYPE_1;
        }
        else if(c1.type2 == c2.type2){
            return Clone.Type.TYPE_2;
        }
        else if(c1.type3 == c2.type3){
            return Clone.Type.TYPE_3;
        }

        return Clone.Type.NONE;
    }

    static class CloneHash {
        final KeywordDefinition keyword;
        final int type1;
        final int type2;
        final int type3;

        CloneHash(KeywordDefinition keyword){
            this.keyword = keyword;
            this.type1 = hash(Clone.Type.TYPE_1);
            this.type2 = hash(Clone.Type.TYPE_2);
            this.type3 = hash(Clone.Type.TYPE_3);
        }

        private int hash(Clone.Type type){
            return this.keyword.getSteps().stream().map(s -> toString(s, type)).filter(s -> !s.isEmpty()).reduce(7, this::accumulate, Integer::sum);
        }

        private int accumulate(int result, String string) {
            return 31 * result + string.hashCode();
        }

        private String toString(Step step, Clone.Type type){
            StringBuilder builder = new StringBuilder();

            switch (type){
                case TYPE_1: buildType1(builder, step); break;
                case TYPE_2: buildType2(builder, step); break;
                case TYPE_3: buildType3(builder, step); break;
            }

            return builder.toString();
        }

        private void buildType1(StringBuilder builder, Step step){
            builder.append(step.toString());
            builder.append("\n");

            for(Step child: step.getSteps()){
                buildType1(builder, child);
            }
        }

        private void buildType2(StringBuilder builder, Step step){
            appendReturnValues(builder, step);
            appendCall(builder, step);
            builder.append("\n");

            for(Step child: step.getSteps()){
                buildType1(builder, child);
            }
        }

        private void buildType3(StringBuilder builder, Step step){
            if(isLog(step)){
               return;
            }

            appendReturnValues(builder, step);
            appendCall(builder, step);
            builder.append("\n");

            for(Step child: step.getSteps()){
                buildType1(builder, child);
            }
        }

        private void appendReturnValues(StringBuilder builder, Step step){
            if(step instanceof Assignment){
                final int valueCount = ((Assignment)step).getLeftHandOperand().size();
                for(int i = 0; i < valueCount; ++i){
                    builder.append("\t");
                    builder.append(String.format("[arg_%d]", i));
                }
            }
        }

        private void appendCall(StringBuilder builder, Step step){
            if(step instanceof ForLoop){
                builder.append("FOR:\t");
            }

            final Optional<KeywordCall> call = step.getKeywordCall();

            if(call.isPresent()){
                call.get().getKeyword().ifPresent(value -> builder.append(value.getName()));
                final int argumentCount = call.get().getArgumentList().size();
                for(int i = 0; i < argumentCount; ++i){
                    builder.append(String.format("[var_%d]", i));
                    builder.append("\t");
                }

            }
        }

        private boolean isLog(Step step){
            return step.getKeywordCall()
                    .map(KeywordCall::getKeywordType)
                    .map(t -> t == Keyword.Type.LOG)
                    .orElse(false);
        }
    }
}
