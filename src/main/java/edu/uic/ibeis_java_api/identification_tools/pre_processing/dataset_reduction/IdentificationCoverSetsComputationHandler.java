package edu.uic.ibeis_java_api.identification_tools.pre_processing.dataset_reduction;

import edu.uic.ibeis_java_api.exceptions.HandlerNotExecutedException;
import edu.uic.ibeis_java_api.exceptions.InvalidThresholdTypeException;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecord;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.query_computation.QueryRecordsCollectionWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;

public class IdentificationCoverSetsComputationHandler {

    private QueryRecordsCollectionWrapper queryRecordsCollectionWrapper;
    private IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper;
    private IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper;

    public IdentificationCoverSetsComputationHandler(QueryRecordsCollectionWrapper queryRecordsCollectionWrapper, IbeisDbAnnotationInfosWrapper ibeisDbAnnotationInfosWrapper) throws InvalidThresholdTypeException {
        if (ibeisDbAnnotationInfosWrapper.getThresholdType() != ThresholdType.ONE_VS_ONE) throw new InvalidThresholdTypeException();
        this.queryRecordsCollectionWrapper = queryRecordsCollectionWrapper;
        this.ibeisDbAnnotationInfosWrapper = ibeisDbAnnotationInfosWrapper;
        this.coverSetsCollectionWrapper = new IdentificationCoverSetsCollectionWrapper(ibeisDbAnnotationInfosWrapper.getTargetSpecies());
    }

    public IdentificationCoverSetsComputationHandler execute() {
        for(IbeisDbAnnotationInfo ibeisDbAnnotationInfo : ibeisDbAnnotationInfosWrapper.getIbeisDbAnnotationInfosMap().values()) {
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

    public IdentificationCoverSetsCollectionWrapper getResult() throws HandlerNotExecutedException{
        if (coverSetsCollectionWrapper == null) throw new HandlerNotExecutedException();
        return coverSetsCollectionWrapper;
    }
}
