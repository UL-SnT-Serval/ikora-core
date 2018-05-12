package lu.uni.serval.robotframework.report;

import lu.uni.serval.utils.ReportKeywordData;
import lu.uni.serval.utils.tree.LabelTreeNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class ReportFactory {
    public static Report create(final Element robotElement){
        Report report = new Report();

        String generated = robotElement.getAttribute("generated");
        String generator = robotElement.getAttribute("generator");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(generated, formatter);

        report.setCreationTime(dateTime);
        report.setGenerator(generator);

        HashSet<String> types = new HashSet<>(Collections.singletonList("suite"));

        for(Element suiteElement: getChildren(robotElement, types)){
            Suite suite = parseSuite(suiteElement, dateTime);

            if(suite == null){
                continue;
            }

            report.addSuite(suite);
        }

        return report;
    }

    private static Suite parseSuite(final Element suiteElement, final LocalDateTime dateTime){
        Suite suite = new Suite();
        suite.setName(suiteElement.getAttribute("name"));
        suite.setId(suiteElement.getAttribute("id"));
        suite.setSource(suiteElement.getAttribute("source"));

        HashSet<String> types = new HashSet<>(Arrays.asList("suite", "kw", "test"));

        for(Element child: getChildren(suiteElement, types)){
            String tagName = child.getTagName();
            if(tagName.equalsIgnoreCase("suite")){
                Suite subSuite = parseSuite(child, dateTime);

                if(subSuite == null){
                    continue;
                }

                suite.addSuite(subSuite);
            }
            else if(tagName.equalsIgnoreCase("kw") || tagName.equalsIgnoreCase("test")){
                String id = suiteElement.getAttribute("id");

                LabelTreeNode keyword = parseKeyword(suite.getSource(), child, dateTime);

                if(keyword == null){
                    continue;
                }

                suite.addKeyword(keyword);
            }
        }

        return suite;
    }

    private static LabelTreeNode parseKeyword(String file, final Element keywordElement, final LocalDateTime dateTime) {
        ReportKeywordData data = new ReportKeywordData();
        data.type = keywordElement.getAttribute("type");
        data.file = file;
        data.name = keywordElement.getAttribute("name");
        data.library = keywordElement.getAttribute("library");

        LabelTreeNode treeNode = new LabelTreeNode(data);

        HashSet<String> types = new HashSet<>(Arrays.asList("doc", "kw", "arguments", "status", "msg"));
        String msg = "";

        for(Element child: getChildren(keywordElement, types)){
            String elementName = child.getTagName();

            if(elementName.equalsIgnoreCase("doc")){
                data.documentation = child.getTextContent();
            }
            else if(elementName.equalsIgnoreCase("kw")){
                LabelTreeNode keyword = parseKeyword(file, child, dateTime);

                if(keyword == null){
                    continue;
                }

                treeNode.add(keyword);
            }
            else if(elementName.equalsIgnoreCase("arguments")){
                data.arguments = parseArguments(child);
            }
            else if(elementName.equalsIgnoreCase("status")){
                data.setStatus(child.getAttribute("status"));
                data.executionDate = dateTime;
            }
            else if(elementName.equalsIgnoreCase("msg")){
                msg = child.getTextContent();
            }
            else{
                System.out.println("Ignored tag '" + elementName + "' while parsing kw");
            }
        }

        if(msg.length() > 0){
            List<String> values = OutputMessageParser.parseArguments(msg, data.name, data.library, data.status);

            for(int index = 0; index < values.size(); ++index){
                data.addVariable(data.arguments.get(index), new ArrayList<>(Collections.singletonList(values.get(index))));
            }
        }

        return treeNode;
    }

    private static List<String> parseArguments(Element argumentsElement) {
        List<String> arguments = new ArrayList<>();

        HashSet<String> types = new HashSet<>(Collections.singletonList("arg"));

        for(Element argumentElement: getChildren(argumentsElement, types)){
           arguments.add(argumentElement.getTextContent());
        }

        return arguments;
    }

    private static List<Element> getChildren(Element parent, Set<String> types){
        NodeList children = parent.getChildNodes();
        int childrenLength = children.getLength();

        List<Element> elements = new ArrayList<>(childrenLength);

        for(int childIndex = 0; childIndex < childrenLength; ++childIndex){
            Node node = children.item(childIndex);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element child = (Element)node;
                if(types.contains(child.getTagName().toLowerCase())){
                    elements.add(child);
                }
            }
        }

        return elements;
    }
}
