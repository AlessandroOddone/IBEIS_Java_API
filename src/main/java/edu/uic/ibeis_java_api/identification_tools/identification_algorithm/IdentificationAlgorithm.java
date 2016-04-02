package edu.uic.ibeis_java_api.identification_tools.identification_algorithm;

import edu.uic.ibeis_java_api.api.Ibeis;
import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisQueryResult;
import edu.uic.ibeis_java_api.api.IbeisQueryScore;
import edu.uic.ibeis_java_api.exceptions.EmptyListParameterException;
import edu.uic.ibeis_java_api.exceptions.InvalidThresholdTypeException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.identification_algorithm.result.IdentificationAlgorithmResult;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;
import edu.uic.ibeis_java_api.values.Species;

import java.io.IOException;
import java.util.List;

public class IdentificationAlgorithm {
    private IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper;
    private IdentificationAlgorithmType algorithmType;
    private double minIndividualRecognitionThreshold;
    private double minSpeciesRecognitionThreshold;
    private double individualIdentificationFactor;
    private double speciesIdentificationFactor;
    private boolean noCache;

    private Ibeis ibeis = new Ibeis();

    public IdentificationAlgorithm(IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper, IdentificationAlgorithmType algorithmType) throws InvalidThresholdTypeException {
        this(ibeisDbAnnotationInfosWrapper, algorithmType, 0, 0, 1, 1, false);
    }

    public IdentificationAlgorithm(IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper, IdentificationAlgorithmType algorithmType,
                                       double minIndividualRecognitionThreshold, double minSpeciesRecognitionThreshold,
                                       double individualIdentificationFactor, double speciesIdentificationFactor, boolean noCache) throws InvalidThresholdTypeException {
        this.ibeisDbAnnotationInfosWrapper = ibeisDbAnnotationInfosWrapper;
        this.algorithmType = algorithmType;
        this.minIndividualRecognitionThreshold = minIndividualRecognitionThreshold;
        this.minSpeciesRecognitionThreshold = minSpeciesRecognitionThreshold;
        this.individualIdentificationFactor = individualIdentificationFactor;
        this.speciesIdentificationFactor = speciesIdentificationFactor;
        this.noCache = noCache;

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

    public IdentificationAlgorithmResult execute(IbeisAnnotation queryAnnotation) throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException, EmptyListParameterException {
        switch (algorithmType) {
            case BEST_SCORE:
                if (noCache) return executeBestScoreNoCache(queryAnnotation);
                return executeBestScore(queryAnnotation);
            case THRESHOLDS_ONE_VS_ONE:
                if (noCache) return executeThresholdsOneVsOneNoCache(queryAnnotation);
                return executeThresholdsOneVsOne(queryAnnotation);
            case THRESHOLDS_ONE_VS_ALL:
                if (noCache) return executeThresholdsOneVsAllNoCache(queryAnnotation);
                return executeThresholdsOneVsAll(queryAnnotation);
        }
        return null;
    }

    private IdentificationAlgorithmResult executeBestScoreNoCache(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException, EmptyListParameterException {
        IbeisQueryResult queryResult = ibeis.queryNoCache(queryAnnotation, ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList());
        List<IbeisQueryScore> queryScores = queryResult.getScores();
        //System.out.println("QUERY RESULT: " + queryResult);

        IbeisQueryScore highestScore = queryScores.get(0);
        for (int i=1; i<queryScores.size(); i++) {
            IbeisQueryScore currentScore = queryScores.get(i);
            if (currentScore.getScore() > highestScore.getScore()) {
                highestScore = currentScore;
            }
        }

        IdentificationAlgorithmResult result = new IdentificationAlgorithmResult(highestScore.getDbAnnotation().getIndividual(),
                ibeisDbAnnotationInfosWrapper.getTargetSpecies());
        return result;
    }

    private IdentificationAlgorithmResult executeBestScore(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException, EmptyListParameterException {
        IbeisQueryResult queryResult = ibeis.query(queryAnnotation, ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList());
        List<IbeisQueryScore> queryScores = queryResult.getScores();
        //System.out.println("QUERY RESULT: " + queryResult);

        IbeisQueryScore highestScore = queryScores.get(0);
        for (int i=1; i<queryScores.size(); i++) {
            IbeisQueryScore currentScore = queryScores.get(i);
            if (currentScore.getScore() > highestScore.getScore()) {
                highestScore = currentScore;
            }
        }

        IdentificationAlgorithmResult result = new IdentificationAlgorithmResult(highestScore.getDbAnnotation().getIndividual(),
                ibeisDbAnnotationInfosWrapper.getTargetSpecies());
        return result;
    }


    private IdentificationAlgorithmResult executeThresholdsOneVsOneNoCache(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException, EmptyListParameterException {
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
            return result;
        }
        result = new IdentificationAlgorithmResult();
        return result;
    }

    private IdentificationAlgorithmResult executeThresholdsOneVsOne(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException, EmptyListParameterException {

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

    private IdentificationAlgorithmResult executeThresholdsOneVsAllNoCache(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException, EmptyListParameterException {
        IbeisQueryResult queryResult = ibeis.queryNoCache(queryAnnotation, ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList());
        List<IbeisQueryScore> queryScores = queryResult.getScores();

        double bestScoreIdentifiedIndividual = 0;
        IdentificationAlgorithmResult identificationAlgorithmResult = new IdentificationAlgorithmResult(Species.UNKNOWN);

        boolean isOfTargetSpecies = false;
        for (IbeisQueryScore queryScore : queryScores) {
            double score = queryScore.getScore();
            IbeisDbAnnotationInfo ibeisDbAnnotationInfo = ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().get(queryScore.getDbAnnotation().getId());
            if (score > bestScoreIdentifiedIndividual && score >= individualIdentificationFactor * ibeisDbAnnotationInfo.getRecognitionThreshold() &&
                    ibeisDbAnnotationInfo.getRecognitionThreshold() >= minIndividualRecognitionThreshold) {
                identificationAlgorithmResult.setIndividual(queryScore.getDbAnnotation().getIndividual());
                identificationAlgorithmResult.setSpecies(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
                bestScoreIdentifiedIndividual = score;
                isOfTargetSpecies = true;
            }
            if (!isOfTargetSpecies) {
                if (score >= speciesIdentificationFactor * ibeisDbAnnotationInfo.getIsOfTargetSpeciesThreshold() &&
                        ibeisDbAnnotationInfo.getIsOfTargetSpeciesThreshold() >= minSpeciesRecognitionThreshold) {
                    identificationAlgorithmResult.setSpecies(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
                    isOfTargetSpecies = true;
                }
            }
        }
        return identificationAlgorithmResult;
    }

    private IdentificationAlgorithmResult executeThresholdsOneVsAll(IbeisAnnotation queryAnnotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException, EmptyListParameterException {
        IbeisQueryResult queryResult = ibeis.query(queryAnnotation, ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationList());
        List<IbeisQueryScore> queryScores = queryResult.getScores();

        double bestScoreIdentifiedIndividual = 0;
        IdentificationAlgorithmResult identificationAlgorithmResult = new IdentificationAlgorithmResult(Species.UNKNOWN);

        boolean isOfTargetSpecies = false;
        for (IbeisQueryScore queryScore : queryScores) {
            double score = queryScore.getScore();
            IbeisDbAnnotationInfo ibeisDbAnnotationInfo = ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().get(queryScore.getDbAnnotation().getId());
            if (score > bestScoreIdentifiedIndividual && score >= individualIdentificationFactor * ibeisDbAnnotationInfo.getRecognitionThreshold() &&
                    ibeisDbAnnotationInfo.getRecognitionThreshold() >= minIndividualRecognitionThreshold) {
                identificationAlgorithmResult.setIndividual(queryScore.getDbAnnotation().getIndividual());
                identificationAlgorithmResult.setSpecies(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
                bestScoreIdentifiedIndividual = score;
                isOfTargetSpecies = true;
            }
            if (!isOfTargetSpecies) {
                if (score >= speciesIdentificationFactor * ibeisDbAnnotationInfo.getIsOfTargetSpeciesThreshold() &&
                        ibeisDbAnnotationInfo.getIsOfTargetSpeciesThreshold() >= minSpeciesRecognitionThreshold) {
                    identificationAlgorithmResult.setSpecies(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
                    isOfTargetSpecies = true;
                }
            }
        }
        return identificationAlgorithmResult;
    }
}
