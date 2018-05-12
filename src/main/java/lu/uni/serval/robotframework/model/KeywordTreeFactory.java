package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.*;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.*;

public class KeywordTreeFactory {

    private Project project;
    private Set<UserKeyword> processed;

    public KeywordTreeFactory(Project project) {
        this.project = project;
    }

    public Set<LabelTreeNode> create() throws DuplicateNodeException {
        Set<LabelTreeNode> keywordForest = new HashSet<>();
        this.processed = new HashSet<>();

        for(TestCaseFile testCaseFile: project.getTestCaseFiles()){
            readFile(testCaseFile, keywordForest);
        }

        return keywordForest;
    }

    private void readFile(TestCaseFile testCaseFile, Set<LabelTreeNode> keywordForest) throws DuplicateNodeException {
        ArrayList<UserKeyword> keywords = new ArrayList<>();
        keywords.addAll(testCaseFile.getTestCases());
        keywords.addAll(testCaseFile.getUserKeywords());

        for(UserKeyword keyword : keywords){
            if(processed.contains(keyword)){
                 continue;
            }

            processed.add(keyword);

            KeywordData keywordData = createKeywordData(testCaseFile, keyword, null, null);
            LabelTreeNode root = new LabelTreeNode(keywordData);

            visitSteps(testCaseFile, root, keyword, new HashMap<>());

            keywordForest.add(root);
        }
    }

    private void visitSteps(TestCaseFile testCaseFile, LabelTreeNode current, UserKeyword userKeyword, HashMap<String, List<String>> locals) throws DuplicateNodeException {
        for(Step step : userKeyword) {
            UserKeyword keyword = testCaseFile.findUserKeyword(step);
            KeywordData keywordData = createKeywordData(testCaseFile, keyword, step, locals);

            LabelTreeNode child =  current.add(keywordData);
            visitSteps(testCaseFile, child, keyword, (HashMap)locals.clone());
        }
    }

    private void setArguments(TestCaseFile testCaseFile, UserKeyword keyword, KeywordData keywordData, Step step, Map<String, List<String>> locals){
        for (Argument argument : keyword.getArguments()) {

            if(argument.hasVariable()) {
                Map<String, List<String>> values = testCaseFile.getVariableValues(argument);

                for(Map.Entry<String, List<String>> value: values.entrySet()){
                    keywordData.addVariable(value.getKey(), value.getValue());
                }
            }

            keywordData.arguments.add(argument.toString());
        }

        if(locals != null) {
            if(step != null) {
                locals.putAll(step.fetchVariables(keyword));
            }

            for(Map.Entry<String, List<String>> value: locals.entrySet()){
                keywordData.addVariable(value.getKey(), value.getValue());
            }
        }
    }

    private void setName(TestCaseFile testCaseFile, UserKeyword keyword, KeywordData keywordData) {
        Argument name = keyword.getName();

        if(name.hasVariable()) {
            Map<String, List<String>> values = testCaseFile.getVariableValues(name);

            for(Map.Entry<String, List<String>> value: values.entrySet()){
                keywordData.addVariable(value.getKey(), value.getValue());
            }
        }

        keywordData.name  = name.toString();
    }

    private KeywordData createKeywordData(TestCaseFile testCaseFile, UserKeyword keyword, Step step, Map<String, List<String>> locals) {
        KeywordData keywordData = new KeywordData();
        keywordData.file = keyword.getFile();
        keywordData.documentation = keyword.getDocumentation();
        setName(testCaseFile, keyword, keywordData);
        setArguments(testCaseFile, keyword, keywordData, step, locals);

        return keywordData;
    }
}
