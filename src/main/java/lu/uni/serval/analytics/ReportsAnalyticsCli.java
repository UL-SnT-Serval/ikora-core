package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.report.OutputParser;
import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;
import lu.uni.serval.utils.exception.DuplicateNodeException;

import java.util.List;

public class ReportsAnalyticsCli implements CommandRunner {
    @Override
    public void run() throws DuplicateNodeException {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("report analytics");
        String location = (String)analytics.getAddictionalProperty("report location", "");

        List<Report> reports = OutputParser.parse(location);
    }
}