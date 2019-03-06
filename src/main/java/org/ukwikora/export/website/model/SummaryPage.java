package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;
import org.ukwikora.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SummaryPage extends Page {
    private BarChart linesChart;
    private BarChart userKeywordsChart;
    private BarChart testCasesChart;
    private List<String> scripts;

    private int linesOfCode;
    private int numberKeywords;
    private int numberTestCases;

    public SummaryPage(String id, String name, List<Project> projects) throws Exception {
        super(id, name);

        linesOfCode = 0;
        numberKeywords = 0;
        numberTestCases = 0;

        int size = projects.size();

        List<String> labels = new ArrayList<>(size);
        List<Integer> lines = new ArrayList<>(size);
        List<Integer> userKeywords = new ArrayList<>(size);
        List<Integer> testCases = new ArrayList<>(size);

        for(Project project: projects){
            linesOfCode += project.getLoc();
            numberKeywords += project.getUserKeywords().size();
            numberTestCases += project.getTestCases().size();

            String projectName = project.getName();

            labels.add(StringUtils.toBeautifulName(projectName));
            lines.add(project.getLoc());
            userKeywords.add(project.getUserKeywords().size());
            testCases.add(project.getTestCases().size());
        }

        linesChart = new BarChart(
                "summary-lines-of-code-chart",
                "Lines of Code",
                lines,
                labels);

        linesChart.setYLabel("Number Lines of Code");

        userKeywordsChart = new BarChart(
                "summary-user-keywords-chart",
                "User Keywords",
                userKeywords,
                labels);

        userKeywordsChart.setYLabel("Number User Keywords");

        testCasesChart = new BarChart(
                "summary-test-cases-chart",
                "Test Cases",
                testCases,
                labels);

        testCasesChart.setYLabel("Number of Test Cases");

        setChartsHeight();

        scripts = new ArrayList<>(3);
        scripts.add(linesChart.getUrl());
        scripts.add(userKeywordsChart.getUrl());
        scripts.add(testCasesChart.getUrl());
    }

    private void setChartsHeight(){
        int height = 600;

        linesChart.setHeight(height);
        userKeywordsChart.setHeight(height);
        testCasesChart.setHeight(height);
    }

    public BarChart getLinesChart() {
        return linesChart;
    }

    public BarChart getUserKeywordsChart() {
        return userKeywordsChart;
    }

    public BarChart getTestCasesChart() {
        return testCasesChart;
    }

    public List<String> getScripts() {
        return scripts;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getNumberKeywords() {
        return numberKeywords;
    }

    public int getNumberTestCases() {
        return numberTestCases;
    }
}
