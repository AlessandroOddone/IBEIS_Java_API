package edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation;

import edu.uic.ibeis_java_api.api.*;
import edu.uic.ibeis_java_api.exceptions.EmptyListParameterException;
import edu.uic.ibeis_java_api.exceptions.HandlerNotExecutedException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.values.Species;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler class to calculate query results given a list of query annotations and a list of database annotations
 */
public class QueryHandler {

    private Ibeis ibeis = new Ibeis();

    private List<IbeisAnnotation> queryAnnotations;
    private List<IbeisAnnotation> dbAnnotations;
    private QueryType queryType;
    private List<String> sameSpeciesOutsiderNames;
    private List<String> differentSpeciesOutsiderNames;
    private QueryRecordsCollectionWrapper queryRecordsCollectionWrapper;

    private int currentQueryIndex;
    private int currentDbIndex;
    private int currentQueryResultIndex;

    /**
     *
     * @param queryAnnotations
     * @param dbAnnotations
     * @param targetSpecies Species of the population of individuals that is being analysed
     * @param sameSpeciesOutsiderNames Names of the individuals of species 'targetSpecies' that are not to be considered as part of the population of individuals that is being analysed
     * @param differentSpeciesOutsiderNames Names of the individuals of species different from 'targetSpecies' that are not to be considered as part of the population of individuals that is being analysed
     */
    public QueryHandler(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation> dbAnnotations, Species targetSpecies,
                        List<String> sameSpeciesOutsiderNames, List<String> differentSpeciesOutsiderNames) {
        this.queryAnnotations = queryAnnotations;
        this.dbAnnotations = dbAnnotations;
        this.queryType = QueryType.ONE_VS_ALL;
        this.queryRecordsCollectionWrapper = new QueryRecordsCollectionWrapper(QueryType.ONE_VS_ALL, targetSpecies);
        this.sameSpeciesOutsiderNames = sameSpeciesOutsiderNames != null ? sameSpeciesOutsiderNames : new ArrayList<String>();
        this.differentSpeciesOutsiderNames = differentSpeciesOutsiderNames != null ? differentSpeciesOutsiderNames : new ArrayList<String>();
    }

    /**
     *
     * @param queryAnnotations
     * @param dbAnnotations
     * @param targetSpecies Species of the population of individuals that is being analysed
     * @param queryType Type of query strategy to be executed
     * @param sameSpeciesOutsiderNames Names of the individuals of species 'targetSpecies' that are not to be considered as part of the population of individuals that is being analysed
     * @param differentSpeciesOutsiderNames Names of the individuals of species different from 'targetSpecies' that are not to be considered as part of the population of individuals that is being analysed
     */
    public QueryHandler(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation> dbAnnotations, Species targetSpecies,
                        QueryType queryType, List<String> sameSpeciesOutsiderNames, List<String> differentSpeciesOutsiderNames) {
        this.queryAnnotations = queryAnnotations;
        this.dbAnnotations = dbAnnotations;
        this.queryType = queryType;
        this.queryRecordsCollectionWrapper = new QueryRecordsCollectionWrapper(queryType, targetSpecies);
        this.sameSpeciesOutsiderNames = sameSpeciesOutsiderNames;
        this.differentSpeciesOutsiderNames = differentSpeciesOutsiderNames;
    }

    public QueryHandler execute(File queryRecordsCollectionWrapperFile) throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException, EmptyListParameterException {
        switch (queryType) {
            case ONE_VS_ALL:
                return executeOneVsAll(queryRecordsCollectionWrapperFile);

            case ONE_VS_ONE:
                return executeOneVsOne(queryRecordsCollectionWrapperFile);
        }
        return null;
    }

    private QueryHandler executeOneVsAll(File queryRecordsCollectionWrapperFile) throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException, EmptyListParameterException {
        try {
            List<IbeisQueryResult> ibeisQueryResultList = ibeis.query(queryAnnotations, dbAnnotations);

            QueryRecordsCollectionWrapper temp = readRecordsCollectionWrapperFromFile(queryRecordsCollectionWrapperFile);
            if (temp != null && !temp.getRecords().isEmpty()) {
                queryRecordsCollectionWrapper = temp;
            }
            currentQueryResultIndex =  getCurrentQueryResultIndexIndex(ibeisQueryResultList, queryRecordsCollectionWrapper);

            int QUERY_RESULT_INDEX = currentQueryResultIndex;
            int NUM_QUERY_RESULTS = ibeisQueryResultList.size();

            for (int i=0; i<ibeisQueryResultList.size(); i++) {
                QUERY_RESULT_INDEX++;

                IbeisQueryResult result = ibeisQueryResultList.get(i);

                IbeisAnnotation queryAnnotation = result.getQueryAnnotation();
                IbeisIndividual queryAnnotationIndividual = queryAnnotation.getIndividual();
                List<IbeisQueryScore> ibeisQueryScoreList = result.getScores();

                int QUERY_SCORE_INDEX = 0;
                int NUM_QUERY_SCORES = ibeisQueryScoreList.size();

                for (IbeisQueryScore score : ibeisQueryScoreList) {
                    System.out.println("QUERY RESULT " + QUERY_RESULT_INDEX + "/" + NUM_QUERY_RESULTS + ": QUERY SCORE " +
                            QUERY_SCORE_INDEX++ + "/" + NUM_QUERY_SCORES);

                    IbeisAnnotation dbAnnotation = score.getDbAnnotation();
                    IbeisIndividual dbAnnotationIndividual = dbAnnotation.getIndividual();

                    boolean queryAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames != null ? sameSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName()) : false;
                    boolean queryAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames != null ? differentSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName()) : false;
                    boolean dbAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames != null ? sameSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName()) : false;
                    boolean dbAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames != null ? differentSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName()) : false;

                    QueryRecord queryRecord = new QueryRecord();
                    queryRecord.setQueryAnnotation(queryAnnotation);
                    queryRecord.setDbAnnotation(dbAnnotation);
                    queryRecord.setScore(score.getScore());
                    queryRecord.setQueryAnnotationOfTargetSpecies(!queryAnnotationDifferentSpeciesOutsider);
                    queryRecord.setDbAnnotationOfTargetSpecies(!dbAnnotationDifferentSpeciesOutsider);
                    queryRecord.setQueryAnnotationOutsider(queryAnnotationSameSpeciesOutsider || queryAnnotationDifferentSpeciesOutsider);
                    queryRecord.setDbAnnotationOutsider(dbAnnotationSameSpeciesOutsider || dbAnnotationDifferentSpeciesOutsider);

                    if (queryAnnotationIndividual.getId() == dbAnnotationIndividual.getId() && !queryAnnotationSameSpeciesOutsider &&
                            !queryAnnotationDifferentSpeciesOutsider && !dbAnnotationSameSpeciesOutsider && !dbAnnotationDifferentSpeciesOutsider) {
                        queryRecord.setSameIndividual(true);
                    }
                    queryRecordsCollectionWrapper.add(queryRecord);
                }
                writeRecordsCollectionWrapperToFile(queryRecordsCollectionWrapperFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
            queryRecordsCollectionWrapper = null;
            throw e;
        } catch (MalformedHttpRequestException e) {
            e.printStackTrace();
            queryRecordsCollectionWrapper = null;
            throw e;
        } catch (UnsuccessfulHttpRequestException e) {
            e.printStackTrace();
            queryRecordsCollectionWrapper = null;
            throw e;
        }
        return this;
    }

    private int getCurrentQueryResultIndexIndex(List<IbeisQueryResult> ibeisQueryResultList, QueryRecordsCollectionWrapper queryRecordsCollectionWrapper) {
        if (queryRecordsCollectionWrapper != null && queryRecordsCollectionWrapper.getRecords().size()>0) {
            IbeisAnnotation currentQueryAnnot = queryRecordsCollectionWrapper.getRecords().get(queryRecordsCollectionWrapper.getRecords().size() - 1).getQueryAnnotation();
            for (int i=0; i<ibeisQueryResultList.size(); i++) {
                if (currentQueryAnnot.getId() == ibeisQueryResultList.get(i).getQueryAnnotation().getId()) {
                    return i;
                }
            }
        }
        return 0;
    }

    private QueryHandler executeOneVsOne(File queryRecordsCollectionWrapperFile) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException, EmptyListParameterException {

        QueryRecordsCollectionWrapper temp = readRecordsCollectionWrapperFromFile(queryRecordsCollectionWrapperFile);
        if (temp != null && !temp.getRecords().isEmpty()) {
            queryRecordsCollectionWrapper = temp;
            currentQueryIndex =  queryAnnotations.indexOf(queryRecordsCollectionWrapper.getRecords().get(queryRecordsCollectionWrapper.getRecords().size() - 1).getQueryAnnotation());
            currentDbIndex = dbAnnotations.indexOf(queryRecordsCollectionWrapper.getRecords().get(queryRecordsCollectionWrapper.getRecords().size() - 1).getDbAnnotation()) + 1;
            if (currentDbIndex >= dbAnnotations.size()) {
                currentQueryIndex++;
                currentDbIndex = 0;
            }
        } else {
            currentQueryIndex = 0;
            currentDbIndex = 0;
        }

        IbeisAnnotation queryAnnotation;
        IbeisAnnotation dbAnnotation;
        IbeisIndividual queryAnnotationIndividual;
        for (int i=currentQueryIndex; i<queryAnnotations.size(); i++) {
            queryAnnotation = queryAnnotations.get(i);
            queryAnnotationIndividual = queryAnnotation.getIndividual();
            for (int j=currentDbIndex; j<dbAnnotations.size(); j++) {
                dbAnnotation = dbAnnotations.get(j);
                IbeisIndividual dbAnnotationIndividual = dbAnnotation.getIndividual();
                if(queryAnnotation.getId() != dbAnnotation.getId()) {
                    IbeisQueryScore ibeisQueryScore;
                    try {
                        List<IbeisQueryScore> ibeisQueryScoreList = ibeis.query(queryAnnotation, dbAnnotation).getScores();
                        if (!ibeisQueryScoreList.isEmpty()) {
                            ibeisQueryScore = ibeisQueryScoreList.get(0);
                        } else {
                            continue;
                        }

                    } catch (UnsuccessfulHttpRequestException e) {
                        e.printStackTrace();
                        continue;
                    }
                    double score = ibeisQueryScore.getScore();

                    boolean queryAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames != null ? sameSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName()) : false;
                    boolean queryAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames != null ? differentSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName()) : false;
                    boolean dbAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames != null ? sameSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName()) : false;
                    boolean dbAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames != null ? differentSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName()) : false;

                    QueryRecord queryRecord = new QueryRecord();
                    queryRecord.setQueryAnnotation(queryAnnotation);
                    queryRecord.setDbAnnotation(dbAnnotation);
                    queryRecord.setScore(score);
                    queryRecord.setQueryAnnotationOfTargetSpecies(!queryAnnotationDifferentSpeciesOutsider);
                    queryRecord.setDbAnnotationOfTargetSpecies(!dbAnnotationDifferentSpeciesOutsider);
                    queryRecord.setQueryAnnotationOutsider(queryAnnotationSameSpeciesOutsider || queryAnnotationDifferentSpeciesOutsider);
                    queryRecord.setDbAnnotationOutsider(dbAnnotationSameSpeciesOutsider || dbAnnotationDifferentSpeciesOutsider);

                    if (queryAnnotationIndividual.getId() == dbAnnotationIndividual.getId() && !queryAnnotationSameSpeciesOutsider &&
                            !queryAnnotationDifferentSpeciesOutsider && !dbAnnotationSameSpeciesOutsider && !dbAnnotationDifferentSpeciesOutsider) {
                        queryRecord.setSameIndividual(true);
                    }
                    queryRecordsCollectionWrapper.add(queryRecord);
                    writeRecordsCollectionWrapperToFile(queryRecordsCollectionWrapperFile);
                }
            }
            currentDbIndex = 0;
        }
        return this;
    }

    public QueryRecordsCollectionWrapper getQueryRecordsCollectionWrapper() throws HandlerNotExecutedException {
        if (queryRecordsCollectionWrapper == null) throw new HandlerNotExecutedException();
        return queryRecordsCollectionWrapper;
    }

    private void writeRecordsCollectionWrapperToFile(File file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(queryRecordsCollectionWrapper.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private QueryRecordsCollectionWrapper readRecordsCollectionWrapperFromFile(File file) {
        BufferedReader reader = null;
        QueryRecordsCollectionWrapper queryRecordsCollectionWrapper = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            queryRecordsCollectionWrapper = QueryRecordsCollectionWrapper.fromJson(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return queryRecordsCollectionWrapper;
    }
}
