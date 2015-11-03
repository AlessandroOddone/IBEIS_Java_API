package edu.uic.ibeis_java_api.identification_tools.thresholds;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.identification_tools.IbeisAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisAnnotationInfoMapWrapper;
import edu.uic.ibeis_java_api.identification_tools.query.QueryRecord;
import edu.uic.ibeis_java_api.identification_tools.query.QueryRecordsCollectionWrapper;
import edu.uic.ibeis_java_api.identification_tools.query.QueryType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class IdentificationThresholdsCalculationHandler {

    private QueryRecordsCollectionWrapper queryRecordsCollectionWrapper;
    private IbeisAnnotationInfoMapWrapper ibeisAnnotationInfoMapWrapper;

    public IdentificationThresholdsCalculationHandler(QueryRecordsCollectionWrapper queryRecordsCollectionWrapper) {
        this.queryRecordsCollectionWrapper = queryRecordsCollectionWrapper;
        ThresholdType thresholdType = queryRecordsCollectionWrapper.getQueryType() == QueryType.ONE_VS_ONE ?
                ThresholdType.ONE_VS_ONE : ThresholdType.WITHIN_DATASET;
        this.ibeisAnnotationInfoMapWrapper = new IbeisAnnotationInfoMapWrapper(thresholdType);
    }

    public IdentificationThresholdsCalculationHandler execute() throws MalformedHttpRequestException, UnsuccessfulHttpRequestException, IOException {
        return execute(30, 0.01);
    }

    public IdentificationThresholdsCalculationHandler execute(int maxThreshold, double step) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        Collection<IbeisAnnotation> dbAnnotations = new ArrayList<>();
        for (QueryRecord queryRecord : queryRecordsCollectionWrapper.getRecords()) {
            dbAnnotations.add(queryRecord.getDbAnnotation());
        }

        for (IbeisAnnotation dbAnnotation : dbAnnotations) {
            double recognitionThreshold = Double.POSITIVE_INFINITY;
            double isGiraffeThreshold = Double.POSITIVE_INFINITY;

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
                            if (queryRecord.isSameGiraffe()) {//true
                                recTP++;
                            } else {//false
                                recFP++;
                            }
                        }
                        //is-a-giraffe threshold
                        if (queryRecord.getScore() >= threshold) {//recognised as giraffe
                            if (queryRecord.isGiraffe()) {//true positive
                                isGiraffeTP++;
                            } else {//false positive
                                isGiraffeFP++;
                            }
                        }
                    }
                }
                double recognitionPrec = (recTP / (recTP + recFP));
                double isGiraffePrec = (isGiraffeTP / (isGiraffeTP + isGiraffeFP));

                if (recognitionThreshold == Double.POSITIVE_INFINITY) {//recognition threshold not found yet
                    if (recognitionPrec > 0.999) {
                        recognitionThreshold = threshold;
                    }
                }
                if (isGiraffeThreshold == Double.POSITIVE_INFINITY) {//is-a-giraffe threshold not found yet
                    if (isGiraffePrec > 0.999) {
                        isGiraffeThreshold = threshold;
                    }
                }
                ibeisAnnotationInfoMapWrapper.add(new IbeisAnnotationInfo(dbAnnotation,
                        dbAnnotation.getIndividual(), isGiraffeThreshold, recognitionThreshold));
            }
        }
        return this;
    }

    public IbeisAnnotationInfoMapWrapper getIbeisAnnotationInfoMapWrapper() {
        return ibeisAnnotationInfoMapWrapper;
    }
}
