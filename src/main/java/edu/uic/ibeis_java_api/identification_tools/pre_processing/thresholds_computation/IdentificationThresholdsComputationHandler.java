package edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.exceptions.HandlerNotExecutedException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecord;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecordsCollectionWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Handler class to calculate thresholds for annotations to be considered as database annotations, given a collection of query results
 */
public class IdentificationThresholdsComputationHandler {

    private QueryRecordsCollectionWrapper queryRecordsCollectionWrapper;
    private IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper;

    public IdentificationThresholdsComputationHandler(QueryRecordsCollectionWrapper queryRecordsCollectionWrapper) {
        this.queryRecordsCollectionWrapper = queryRecordsCollectionWrapper;
        ThresholdType thresholdType = queryRecordsCollectionWrapper.getQueryType() == QueryType.ONE_VS_ONE ?
                ThresholdType.ONE_VS_ONE : ThresholdType.WITHIN_DATASET;
        this.ibeisDbAnnotationInfosWrapper = new IbeisDbAnnotationInfosWrapper(thresholdType, queryRecordsCollectionWrapper.getTargetSpecies());
    }

    public IdentificationThresholdsComputationHandler execute() throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException {
        return execute(30, 0.01);
    }

    public IdentificationThresholdsComputationHandler execute(int maxThreshold, double step) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        Collection<IbeisAnnotation> dbAnnotations = new ArrayList<>();
        for (QueryRecord queryRecord : new HashSet<>(queryRecordsCollectionWrapper.getRecords())) {
            if (!queryRecord.isDbAnnotationOutsider()) {
                dbAnnotations.add(queryRecord.getDbAnnotation());
            } else {
                IbeisDbAnnotationInfo ibeisDbAnnotationInfo = new IbeisDbAnnotationInfo(queryRecord.getDbAnnotation());
                ibeisDbAnnotationInfo.setOfTargetSpecies(queryRecord.isDbAnnotationOfTargetSpecies());
                ibeisDbAnnotationInfo.setOutsider(true);
                ibeisDbAnnotationInfosWrapper.add(ibeisDbAnnotationInfo);
            }
        }

        for (IbeisAnnotation dbAnnotation : dbAnnotations) {
            double recognitionThreshold = Double.POSITIVE_INFINITY;
            double isOfTargetSpeciesThreshold = Double.POSITIVE_INFINITY;

            for (int i = 0; i <= maxThreshold / step; i++) {

                double threshold = i * step;
                double recTP = 0;
                double recFP = 0;
                double isGiraffeTP = 0;
                double isGiraffeFP = 0;

                for (QueryRecord queryRecord : queryRecordsCollectionWrapper.getRecords()) {
                    //System.out.println("QUERY RECORD: " + queryRecord);
                    if (queryRecord.getDbAnnotation().getId() == dbAnnotation.getId()) {
                        //recognition threshold
                        if (queryRecord.getScore() >= threshold) {//positive
                            if (queryRecord.isSameIndividual()) {//true
                                recTP++;
                            } else {//false
                                recFP++;
                            }
                        }
                        //is-a-giraffe threshold
                        if (queryRecord.getScore() >= threshold) {//recognised as giraffe
                            if (queryRecord.isQueryAnnotationOfTargetSpecies()) {//true positive
                                isGiraffeTP++;
                            } else {//false positive
                                isGiraffeFP++;
                            }
                        }
                    }
                }                double recognitionPrec = (recTP / (recTP + recFP));
                double isGiraffePrec = (isGiraffeTP / (isGiraffeTP + isGiraffeFP));

                if (recognitionThreshold == Double.POSITIVE_INFINITY) {//recognition threshold not found yet
                    if (recognitionPrec > 0.999) {
                        recognitionThreshold = threshold;
                    }
                }
                if (isOfTargetSpeciesThreshold == Double.POSITIVE_INFINITY) {//is-a-giraffe threshold not found yet
                    if (isGiraffePrec > 0.999) {
                        isOfTargetSpeciesThreshold = threshold;
                    }
                }
                IbeisDbAnnotationInfo ibeisDbAnnotationInfo = new IbeisDbAnnotationInfo(dbAnnotation);
                ibeisDbAnnotationInfo.setIsOfTargetSpeciesThreshold(isOfTargetSpeciesThreshold);
                ibeisDbAnnotationInfo.setRecognitionThreshold(recognitionThreshold);
                ibeisDbAnnotationInfo.setOfTargetSpecies(true);
                ibeisDbAnnotationInfo.setOutsider(false);
                ibeisDbAnnotationInfosWrapper.add(ibeisDbAnnotationInfo);
            }
        }
        return this;
    }

    public IbeisDbAnnotationInfosWrapper getResult() throws HandlerNotExecutedException {
        if (ibeisDbAnnotationInfosWrapper == null) throw new HandlerNotExecutedException();
        return ibeisDbAnnotationInfosWrapper;
    }
}
