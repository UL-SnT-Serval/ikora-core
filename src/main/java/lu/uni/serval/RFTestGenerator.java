package lu.uni.serval;

import lu.uni.serval.robotframework.model.KeywordTreeFactory;
import lu.uni.serval.robotframework.model.TestCaseFile;
import lu.uni.serval.robotframework.model.TestCaseFileFactory;
import lu.uni.serval.robotframework.selenium.Runner;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.TreeNode;

import org.apache.commons.cli.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.ArrayList;
import java.util.List;

public class RFTestGenerator {
    public static void main(String[] args) {
        try {
            Options options = new Options();
            options.addOption("file", true, "path to RobotFramework testcase file");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("file")) {
                throw new MissingArgumentException("file");
            }

            TestCaseFileFactory factory = new TestCaseFileFactory();
            TestCaseFile testCaseFile = factory.create(cmd.getOptionValue("file"));

            KeywordTreeFactory keywordTreeFactory = new KeywordTreeFactory(testCaseFile);
            List<TreeNode<KeywordData>> forest = keywordTreeFactory.create();

            System.out.println(forest.toString());
            System.out.println("----------------------------------------");

            for(TreeNode<KeywordData> root : forest){
                System.out.println(root.data.toString());
                for(TreeNode<KeywordData> leaf : root.getLeaves()) {
                    System.out.println("\t" + leaf.toString());
                }
            }

            Runner runner = new Runner();

            ArrayList<String> arguments = new ArrayList<String>();
            arguments.add("http://localhost:7272/");
            arguments.add("Firefox");

            runner.execute("Open Browser", arguments);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
