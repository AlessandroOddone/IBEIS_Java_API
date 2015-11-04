package edu.uic.ibeis_java_api.identification_tools.pre_processing.dataset_reduction;

import edu.uic.ibeis_java_api.exceptions.InvalidThresholdTypeException;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.IbeisDbAnnotationInfoMapWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecord;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecordsCollectionWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;

public class IdentificationCoverSetsCalculationHandler {

    private QueryRecordsCollectionWrapper queryRecordsCollectionWrapper;
    private IbeisDbAnnotationInfoMapWrapper ibeisDbAnnotationInfoMapWrapper;
    private IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper;

    public IdentificationCoverSetsCalculationHandler(QueryRecordsCollectionWrapper queryRecordsCollectionWrapper, IbeisDbAnnotationInfoMapWrapper ibeisDbAnnotationInfoMapWrapper) throws InvalidThresholdTypeException {
        if (ibeisDbAnnotationInfoMapWrapper.getThresholdType() != ThresholdType.ONE_VS_ONE) throw new InvalidThresholdTypeException();
        this.queryRecordsCollectionWrapper = queryRecordsCollectionWrapper;
        this.ibeisDbAnnotationInfoMapWrapper = ibeisDbAnnotationInfoMapWrapper;
        this.coverSetsCollectionWrapper = new IdentificationCoverSetsCollectionWrapper();
    }

    public IdentificationCoverSetsCalculationHandler execute() {
        for(IbeisDbAnnotationInfo ibeisDbAnnotationInfo : ibeisDbAnnotationInfoMapWrapper.getMap().values()) {
            if (!ibeisDbAnnotationInfo.isOutsider()) {
                IdentificationCoverSet coverSet = new IdentificationCoverSet(ibeisDbAnnotationInfo);
                for(QueryRecord queryRecord : queryRecordsCollectionWrapper.getRecords()) {
                    if(queryRecord.getDbAnnotation().getId() == ibeisDbAnnotationInfo.getAnnotation().getId()) {
                        if(queryRecord.getScore() >= ibeisDbAnnotationInfo.getRecognitionThreshold() && queryRecord.isSameIndividual()) {
                            coverSet.add(queryRecord.getQueryAnnotation());
                        }
                    }
                }
                coverSetsCollectionWrapper.add(coverSet);
            }
        }
        return this;
    }

    public IdentificationCoverSetsCollectionWrapper getResult() {
        return coverSetsCollectionWrapper;
    }
}
