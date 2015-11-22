package edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation;

import edu.uic.ibeis_java_api.api.*;
import edu.uic.ibeis_java_api.exceptions.EmptyListParameterException;
import edu.uic.ibeis_java_api.exceptions.HandlerNotExecutedException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.values.Species;

import java.io.IOException;
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

    private int currentQueryIndex = 0;
    private int currentDbIndex = 0;

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
        this.sameSpeciesOutsiderNames = sameSpeciesOutsiderNames;
        this.differentSpeciesOutsiderNames = differentSpeciesOutsiderNames;
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
    @Deprecated
    public QueryHandler(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation> dbAnnotations, Species targetSpecies,
                        QueryType queryType, List<String> sameSpeciesOutsiderNames, List<String> differentSpeciesOutsiderNames) {
        this.queryAnnotations = queryAnnotations;
        this.dbAnnotations = dbAnnotations;
        this.queryType = queryType;
        this.queryRecordsCollectionWrapper = new QueryRecordsCollectionWrapper(queryType, targetSpecies);
        this.sameSpeciesOutsiderNames = sameSpeciesOutsiderNames;
        this.differentSpeciesOutsiderNames = differentSpeciesOutsiderNames;
    }

    public QueryHandler execute() throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException, EmptyListParameterException {
        switch (queryType) {
            case ONE_VS_ALL:
                return executeOneVsAll();

            case ONE_VS_ONE:
                return executeOneVsOne();
        }
        return null;
    }

    private QueryHandler executeOneVsAll() throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException, EmptyListParameterException {
        try {
            List<IbeisQueryResult> ibeisQueryResultList = ibeis.query(queryAnnotations, dbAnnotations);

            for (IbeisQueryResult result : ibeisQueryResultList) {
                IbeisAnnotation queryAnnotation = result.getQueryAnnotation();
                IbeisIndividual queryAnnotationIndividual = queryAnnotation.getIndividual();
                List<IbeisQueryScore> ibeisQueryScoreList = result.getScores();

                for (IbeisQueryScore score : ibeisQueryScoreList) {
                    IbeisAnnotation dbAnnotation = score.getDbAnnotation();
                    IbeisIndividual dbAnnotationIndividual = dbAnnotation.getIndividual();

                    boolean queryAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName());
                    boolean queryAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName());
                    boolean dbAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName());
                    boolean dbAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName());

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

    @Deprecated
    private QueryHandler executeOneVsOne() {
        IbeisAnnotation queryAnnotation;
        IbeisAnnotation dbAnnotation;

        try {
            queryAnnotation = queryAnnotations.get(currentQueryIndex);
            IbeisIndividual queryAnnotationIndividual = queryAnnotation.getIndividual();
            for (int j=currentDbIndex; j<dbAnnotations.size(); j++) {
                dbAnnotation = dbAnnotations.get(j);
                currentDbIndex = j;
                IbeisIndividual dbAnnotationIndividual = dbAnnotation.getIndividual();
                if(queryAnnotation.getId() != dbAnnotation.getId()) {
                    IbeisQueryScore ibeisQueryScore = ibeis.query(queryAnnotation, dbAnnotation).getScores().get(0);
                    double score = ibeisQueryScore.getScore();

                    boolean queryAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName());
                    boolean queryAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName());
                    boolean dbAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName());
                    boolean dbAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName());

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
                }
            }
            currentQueryIndex++;

            for (int i=currentQueryIndex; i<queryAnnotations.size(); i++) {
                queryAnnotation = queryAnnotations.get(i);
                queryAnnotationIndividual = queryAnnotation.getIndividual();
                currentQueryIndex = i;
                for (int j=0; j<dbAnnotations.size(); j++) {
                    dbAnnotation = dbAnnotations.get(j);
                    currentDbIndex = j;
                    IbeisIndividual dbAnnotationIndividual = dbAnnotation.getIndividual();
                    if(queryAnnotation.getId() != dbAnnotation.getId()) {
                        IbeisQueryScore ibeisQueryScore = ibeis.query(queryAnnotation, dbAnnotation).getScores().get(0);
                        double score = ibeisQueryScore.getScore();

                        boolean queryAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName());
                        boolean queryAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames.contains(queryAnnotationIndividual.getName());
                        boolean dbAnnotationSameSpeciesOutsider = sameSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName());
                        boolean dbAnnotationDifferentSpeciesOutsider = differentSpeciesOutsiderNames.contains(dbAnnotationIndividual.getName());

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

    public QueryRecordsCollectionWrapper getQueryRecordsCollectionWrapper() throws HandlerNotExecutedException {
        if (queryRecordsCollectionWrapper == null) throw new HandlerNotExecutedException();
        return queryRecordsCollectionWrapper;
    }
}
