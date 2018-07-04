package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Keyword;
import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.report.Report;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

public class ReportAnalyzer implements Iterable<Report>{
    private List<Report> reports;
    private KeywordSequence sequences;
    private StatusResults status;

    public ReportAnalyzer(){
        reports = new ArrayList<>();
        sequences = null;
    }

    public StatusResults getStatus(){
        initStatus();
        return status;
    }

    public void add(Report report){
        boolean inserted = false;

        for(int index = 0; index < reports.size(); ++index){
            if(reports.get(index).getCreationTime().isAfter(report.getCreationTime())){
                reports.add(index, report);
                inserted = true;
                break;
            }
        }

        if(!inserted){
            reports.add(report);
        }

        sequences = null;
        status = null;
    }


    public DifferenceResults findDifferences(){
        initKeywordSequence();

        DifferenceResults differences = new DifferenceResults();

        for(List<KeywordDefinition> sequence: sequences){
            KeywordDefinition previous = null;
            for(KeywordDefinition keyword: sequence){
                if(previous == null){
                    previous = keyword;
                    continue;
                }

                Pair keywordPair = ImmutablePair.of((Keyword)previous, (Keyword)keyword);

                if(differences.containsKey(keywordPair)){
                    previous = keyword;
                    continue;
                }

                Difference difference = Difference.of(previous, keyword);

                /*
                LocalDateTime dateTime = KeywordStatus.getExecutionDate();

                for(EditAction difference : editDistance.differences(previous, keyword)){
                    // check that the change was not already accounted (keywords are reused)
                    if(memory.addDifference(dateTime, difference)){
                        differences.addDifference(difference);
                    }

                }
                */

                previous = keyword;
            }
        }

        return differences;
    }

    private void initKeywordSequence(){
        if(sequences != null){
            return;
        }

        initStatus();

        sequences = new KeywordSequence();

        for(Report report: reports){
/*
            List<KeywordStatus> keywords = report.getKeywords();

            for(KeywordStatus keyword: keywords){
                if(!status.isServiceDown(keyword)){
                    sequences.add(keyword);
                }
            }
*/
        }
    }

    private void initStatus(){
        if(status != null){
            return;
        }

        status = new StatusResults();
/*
        for(Report report: reports){
            List<KeywordStatus> keywords = report.getKeywords();

            for(KeywordStatus keyword: keywords){
                status.add(keyword);
            }
        }
*/
    }

    @Override
    @Nonnull
    public Iterator<Report> iterator() {
        return reports.iterator();
    }

}
