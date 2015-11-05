package edu.uic.ibeis_java_api.identification_tools.identification_algorithm;

import edu.uic.ibeis_java_api.api.Ibeis;
import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisQueryResult;
import edu.uic.ibeis_java_api.api.IbeisQueryScore;
import edu.uic.ibeis_java_api.exceptions.InvalidThresholdTypeException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.identification_algorithm.result.IdentificationAlgorithmResult;
import edu.uic.ibeis_java_api.identification_tools.identification_algorithm.result.TestingModeInfo;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class IdentificationAlgorithm {
    private IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper;
    private IdentificationAlgorithmType algorithmType;
    private boolean testingMode;

    private Ibeis ibeis = new Ibeis();

    public IdentificationAlgorithm(IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper, IdentificationAlgorithmType algorithmType) throws InvalidThresholdTypeException {
        this(ibeisDbAnnotationInfosWrapper, algorithmType , false);
    }

    public IdentificationAlgorithm(IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper, IdentificationAlgorithmType algorithmType, boolean testingMode) throws InvalidThresholdTypeException {
        this.ibeisDbAnnotationInfosWrapper = ibeisDbAnnotationInfosWrapper;
        this.algorithmType = algorithmType;
        this.testingMode = testingMode;

        ThresholdType thresholdType = ibeisDbAnnotationInfosWrapper.getThresholdType();
        switch (algorithmType) {
            case BEST_SCORE:
                if (thresholdType == ThresholdType.ONE_VS_ONE) throw new InvalidThresholdTypeException();
                break;
            case THRESHOLDS_ONE_VS_ONE:
                if (thresholdType == ThresholdType.WITHIN_DATASET) throw new InvalidThresholdTypeException();
                break;
            case THRESHOLDS_ONE_VS_ALL:
                if (thresholdType == ThresholdType.ONE_VS_ONE) throw new InvalidThresholdTypeException();
                break;
        }
    }

    public IdentificationAlgorithmResult execute(IbeisAnnotation queryAnnotation) throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException {
        switch (algorithmType) {
            case BEST_SCORE:
                if (testingMode) return executeBestScoreTestingMode(queryAnnotation);
                return executeBestScore(queryAnnotation);
            case THRESHOLDS_ONE_VS_ONE:
                if (testingMode) return executeBestScoreTestingMode(queryAnnotation);
                return executeThresholdsOneVsOne(queryAnnotation);
            case THRESHOLDS_ONE_VS_ALL:
                if (testingMode) executeThresholdsOneVsAllTestingMode(queryAnnotation);
                return executeThresholdsOneVsAll(queryAnnotation);
        }
        return null;
    }

    private IdentificationAlgorithmResult executeBestScoreTestingMode(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        long startTime = System.nanoTime();

        IbeisQueryResult queryResult = ibeis.queryNoCache(queryAnnotation, ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList());
        List<IbeisQueryScore> queryScores = queryResult.getScores();
        //System.out.println("QUERY RESULT: " + queryResult);

        //sort query scores from the highest to the lowest
        Collections.sort(queryScores, Collections.reverseOrder());
        //get the highest score
        IbeisQueryScore highestScore = queryScores.get(0);
        //System.out.println("HIGHEST SCORE: " + highestScore);

        IdentificationAlgorithmResult result = new IdentificationAlgorithmResult(highestScore.getDbAnnotation().getIndividual(),
                ibeisDbAnnotationInfosWrapper.getTargetSpecies());
        result.setTestingModeInfo(new TestingModeInfo(System.nanoTime() - startTime));
        return result;
    }

    private IdentificationAlgorithmResult executeBestScore(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        IbeisQueryResult queryResult = ibeis.query(queryAnnotation, ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList());
        List<IbeisQueryScore> queryScores = queryResult.getScores();
        //System.out.println("QUERY RESULT: " + queryResult);

        //sort query scores from the highest to the lowest
        Collections.sort(queryScores, Collections.reverseOrder());
        //get the highest score
        IbeisQueryScore highestScore = queryScores.get(0);
        //System.out.println("HIGHEST SCORE: " + highestScore);

        IdentificationAlgorithmResult result = new IdentificationAlgorithmResult(highestScore.getDbAnnotation().getIndividual(),
                ibeisDbAnnotationInfosWrapper.getTargetSpecies());
        return result;
    }


    private IdentificationAlgorithmResult executeThresholdsOneVsOneTestingMode(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        long startTime = System.nanoTime();

        IdentificationAlgorithmResult result;
        boolean isOfTargetSpecies = false;

        for (IbeisDbAnnotationInfo i : ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().values()) {
            IbeisAnnotation dbAnnotation = i.getAnnotation();
            double isOfTargetSpeciesThreshold = i.getIsOfTargetSpeciesThreshold();
            double recognitionThreshold = i.getRecognitionThreshold();

            IbeisQueryResult queryResult = ibeis.queryNoCache(queryAnnotation, dbAnnotation);
            double score = queryResult.getScores().get(0).getScore();

            if (score >= recognitionThreshold) {
                result = new IdentificationAlgorithmResult(dbAnnotation.getIndividual(), ibeisDbAnnotationInfosWrapper.getTargetSpecies());
                result.setTestingModeInfo(new TestingModeInfo(System.nanoTime() - startTime));
                return result;
            }
            if (!isOfTargetSpecies) {
                if (score >= isOfTargetSpeciesThreshold) {
                    isOfTargetSpecies = true;
                }
            }
        }
        if (isOfTargetSpecies) {
            result = new IdentificationAlgorithmResult(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
            result.setTestingModeInfo(new TestingModeInfo(System.nanoTime() - startTime));
            return result;
        }
        result = new IdentificationAlgorithmResult();
        result.setTestingModeInfo(new TestingModeInfo(System.nanoTime() - startTime));
        return result;
    }

    private IdentificationAlgorithmResult executeThresholdsOneVsOne(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {

        boolean isOfTargetSpecies = false;

        for (IbeisDbAnnotationInfo i : ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().values()) {
            IbeisAnnotation dbAnnotation = i.getAnnotation();
            double isOfTargetSpeciesThreshold = i.getIsOfTargetSpeciesThreshold();
            double recognitionThreshold = i.getRecognitionThreshold();

            IbeisQueryResult queryResult = ibeis.query(queryAnnotation, dbAnnotation);
            double score = queryResult.getScores().get(0).getScore();

            if (score >= recognitionThreshold) {
                IdentificationAlgorithmResult result = new IdentificationAlgorithmResult(dbAnnotation.getIndividual(), ibeisDbAnnotationInfosWrapper.getTargetSpecies());
                return result;
            }
            if (!isOfTargetSpecies) {
                if (score >= isOfTargetSpeciesThreshold) {
                    isOfTargetSpecies = true;
                }
            }
        }
        if (isOfTargetSpecies) {
            return new IdentificationAlgorithmResult(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
        }
        return new IdentificationAlgorithmResult();
    }

    private IdentificationAlgorithmResult executeThresholdsOneVsAllTestingMode(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        long startTime = System.nanoTime();

        IdentificationAlgorithmResult result;

        IbeisQueryResult queryResult = ibeis.query(queryAnnotation, ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList());
        List<IbeisQueryScore> queryScores = queryResult.getScores();
        //System.out.println("QUERY RESULT: " + queryResult);

        //sort query scores from the highest to the lowest
        Collections.sort(queryScores, Collections.reverseOrder());
        //get the highest score

        boolean isOfTargetSpecies = false;
        for (IbeisQueryScore queryScore : queryScores) {
            double score = queryScore.getScore();
            if (score <= 0) break;
            IbeisDbAnnotationInfo ibeisDbAnnotationInfo = ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().get(queryScore.getDbAnnotation().getId());
            if (score >= ibeisDbAnnotationInfo.getRecognitionThreshold()) {
                result = new IdentificationAlgorithmResult(queryScore.getDbAnnotation().getIndividual(), ibeisDbAnnotationInfosWrapper.getTargetSpecies());
                result.setTestingModeInfo(new TestingModeInfo(System.nanoTime() - startTime));
                return result;
            }
            if (!isOfTargetSpecies) {
                if (score >= ibeisDbAnnotationInfo.getIsOfTargetSpeciesThreshold()) {
                    isOfTargetSpecies = true;
                }
            }
        }
        if (isOfTargetSpecies) {
            result = new IdentificationAlgorithmResult(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
            result.setTestingModeInfo(new TestingModeInfo(System.nanoTime() - startTime));
            return result;
        }
        result = new IdentificationAlgorithmResult();
        result.setTestingModeInfo(new TestingModeInfo(System.nanoTime() - startTime));
        return result;
    }

    private IdentificationAlgorithmResult executeThresholdsOneVsAll(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        IbeisQueryResult queryResult = ibeis.query(queryAnnotation, ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList());
        List<IbeisQueryScore> queryScores = queryResult.getScores();
        //System.out.println("QUERY RESULT: " + queryResult);

        //sort query scores from the highest to the lowest
        Collections.sort(queryScores, Collections.reverseOrder());
        //get the highest score

        boolean isOfTargetSpecies = false;
        for (IbeisQueryScore queryScore : queryScores) {
            double score = queryScore.getScore();
            if (score <= 0) break;
            IbeisDbAnnotationInfo ibeisDbAnnotationInfo = ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().get(queryScore.getDbAnnotation().getId());
            if (score >= ibeisDbAnnotationInfo.getRecognitionThreshold()) {
                IdentificationAlgorithmResult result = new IdentificationAlgorithmResult(queryScore.getDbAnnotation().getIndividual(), ibeisDbAnnotationInfosWrapper.getTargetSpecies());
                return result;
            }
            if (!isOfTargetSpecies) {
                if (score >= ibeisDbAnnotationInfo.getIsOfTargetSpeciesThreshold()) {
                    isOfTargetSpecies = true;
                }
            }
        }
        if (isOfTargetSpecies) {
            return new IdentificationAlgorithmResult(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
        }
        return new IdentificationAlgorithmResult();
    }
}
