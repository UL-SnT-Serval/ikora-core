package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;

import java.io.*;

public class ProjectAnalyticsCli implements CommandRunner {
    @Override
    public void run() throws Exception {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("report analytics");

        String gitUrl = (String)analytics.getAdditionalProperty("git url", "");
        String localFolder = (String)analytics.getAdditionalProperty("git local path", "");

        ProjectAnalyzer projects = ProjectAnalyzer.parse(gitUrl, localFolder);

        exportDifferences(analytics, projects);
    }

    private void exportDifferences(Plugin analytics, ProjectAnalyzer projects) {
        if(analytics.getAdditionalProperty("output differences file", "").equals("")){
            return;
        }

        DifferenceResults results = projects.findDifferences();

        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File((String)analytics.getAdditionalProperty("output differences file", "./report-differences.json"));

            mapper.writeValue(file, results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
