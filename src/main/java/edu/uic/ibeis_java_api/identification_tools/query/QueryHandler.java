package edu.uic.ibeis_java_api.identification_tools.query;

import edu.uic.ibeis_java_api.api.Ibeis;
import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisQueryResult;
import edu.uic.ibeis_java_api.api.IbeisQueryScore;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public class QueryHandler {

    private Ibeis ibeis = new Ibeis();

    private List<IbeisAnnotation> queryAnnotations;
    private List<IbeisAnnotation> dbAnnotations;
    private QueryType queryType;
    private List<String> sameSpeciesOutsiderNames;
    private List<String> differentSpeciesOutsiderNames;
    private QueryRecordsCollectionWrapper queryRecordsCollectionWrapper;

    private int currentQueryIndex = 0;
    private int currentDbIndex = 0;

    public QueryHandler(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation> dbAnnotations,
                        List<String> sameSpeciesOutsiderNames, List<String> differentSpeciesOutsiderNames) {
        this.queryAnnotations = queryAnnotations;
        this.dbAnnotations = dbAnnotations;
        this.queryRecordsCollectionWrapper = new QueryRecordsCollectionWrapper(QueryType.ONE_VS_ALL);
        this.sameSpeciesOutsiderNames = sameSpeciesOutsiderNames;
        this.differentSpeciesOutsiderNames = differentSpeciesOutsiderNames;
    }

    @Deprecated
    public QueryHandler(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation> dbAnnotations, QueryType queryType,
                        List<String> sameSpeciesOutsiderNames, List<String> differentSpeciesOutsiderNames) {
        this.queryAnnotations = queryAnnotations;
        this.dbAnnotations = dbAnnotations;
        this.queryRecordsCollectionWrapper = new QueryRecordsCollectionWrapper(queryType);
        this.sameSpeciesOutsiderNames = sameSpeciesOutsiderNames;
        this.differentSpeciesOutsiderNames = differentSpeciesOutsiderNames;
    }

    public QueryHandler execute() throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException {
        switch (queryType) {
            case ONE_VS_ALL:
                return executeOneVsAll();

            case ONE_VS_ONE:
                return executeOneVsOne();
        }
        return null;
    }

    private QueryHandler executeOneVsAll() throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        List<IbeisQueryResult> ibeisQueryResultList = ibeis.query(queryAnnotations, dbAnnotations);

        for (IbeisQueryResult result : ibeisQueryResultList) {
            IbeisAnnotation queryAnnotation = result.getQueryAnnotation();
            List<IbeisQueryScore> ibeisQueryScoreList = result.getScores();

            for (IbeisQueryScore score : ibeisQueryScoreList) {
                QueryRecord queryRecord = new QueryRecord();
                queryRecord.setQueryAnnotation(queryAnnotation);
                queryRecord.setDbAnnotation(score.getDbAnnotation());
                queryRecord.setScore(score.getScore());
                queryRecord.setSameGiraffe(queryAnnotation.getIndividual().getId() ==
                        score.getDbAnnotation().getIndividual().getId() ? true : false);
                queryRecord.setGiraffe(differentSpeciesOutsiderNames.contains(queryAnnotation.getIndividual().getName()) ? false : true);

                queryRecordsCollectionWrapper.add(queryRecord);
            }
        }
        return this;
    }

    @Deprecated
    private QueryHandler executeOneVsOne() {
        IbeisAnnotation queryAnnotation;
        IbeisAnnotation dbAnnotation;

        try {
            queryAnnotation = queryAnnotations.get(currentQueryIndex);
            for (int j=currentDbIndex; j<dbAnnotations.size(); j++) {
                dbAnnotation = dbAnnotations.get(j);
                currentDbIndex = j;
                String dbAnnotationIndividualName = dbAnnotation.getIndividual().getName();
                if(queryAnnotation.getId() != dbAnnotation.getId() &&
                        !sameSpeciesOutsiderNames.contains(dbAnnotationIndividualName) &&
                        !differentSpeciesOutsiderNames.contains(dbAnnotationIndividualName)) {
                    IbeisQueryScore ibeisQueryScore = ibeis.query(queryAnnotation, dbAnnotation).getScores().get(0);
                    double score = ibeisQueryScore.getScore();

                    QueryRecord queryRecord = new QueryRecord();
                    queryRecord.setQueryAnnotation(queryAnnotation);
                    queryRecord.setDbAnnotation(ibeisQueryScore.getDbAnnotation());
                    queryRecord.setScore(score);
                    queryRecord.setSameGiraffe(queryAnnotation.getIndividual().getId() ==
                            ibeisQueryScore.getDbAnnotation().getIndividual().getId() ? true : false);
                    queryRecord.setGiraffe(differentSpeciesOutsiderNames.contains(queryAnnotation.getIndividual().getName()) ? false : true);

                    queryRecordsCollectionWrapper.add(queryRecord);
                }
            }
            currentQueryIndex++;

            for (int i=currentQueryIndex; i<queryAnnotations.size(); i++) {
                queryAnnotation = queryAnnotations.get(i);
                currentQueryIndex = i;
                for (int j=0; j<dbAnnotations.size(); j++) {
                    dbAnnotation = dbAnnotations.get(j);
                    currentDbIndex = j;
                    String dbAnnotationIndividualName = dbAnnotation.getIndividual().getName();
                    if(queryAnnotation.getId() != dbAnnotation.getId() &&
                            !sameSpeciesOutsiderNames.contains(dbAnnotationIndividualName) &&
                            !differentSpeciesOutsiderNames.contains(dbAnnotationIndividualName)) {
                        IbeisQueryScore ibeisQueryScore = ibeis.query(queryAnnotation, dbAnnotation).getScores().get(0);
                        double score = ibeisQueryScore.getScore();

                        QueryRecord queryRecord = new QueryRecord();
                        queryRecord.setQueryAnnotation(queryAnnotation);
                        queryRecord.setDbAnnotation(ibeisQueryScore.getDbAnnotation());
                        queryRecord.setScore(score);
                        queryRecord.setSameGiraffe(queryAnnotation.getIndividual().getId() ==
                                ibeisQueryScore.getDbAnnotation().getIndividual().getId() ? true : false);
                        queryRecord.setGiraffe(differentSpeciesOutsiderNames.contains(queryAnnotation.getIndividual().getName()) ? false : true);

                        queryRecordsCollectionWrapper.add(queryRecord);
                    }
                }
            }
        } catch (Exception e) {
            executeOneVsOne();
            e.printStackTrace();
        } finally {
            return this;
        }
    }

    public QueryRecordsCollectionWrapper getQueryRecordsCollectionWrapper() {
        return queryRecordsCollectionWrapper;
    }
}
