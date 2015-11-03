package edu.uic.ibeis_java_api.identification_tools.dataset_reduction;

import edu.uic.ibeis_java_api.exceptions.InvalidThresholdTypeException;
import edu.uic.ibeis_java_api.identification_tools.IbeisAnnotationInfoMapWrapper;
import edu.uic.ibeis_java_api.identification_tools.thresholds.ThresholdType;

public class CoverSetsCalculationHandler {

    private IbeisAnnotationInfoMapWrapper ibeisAnnotationInfoMapWrapper;

    public CoverSetsCalculationHandler(IbeisAnnotationInfoMapWrapper ibeisAnnotationInfoMapWrapper) throws InvalidThresholdTypeException {
        if (ibeisAnnotationInfoMapWrapper.getThresholdType() != ThresholdType.ONE_VS_ONE) throw new InvalidThresholdTypeException();
        this.ibeisAnnotationInfoMapWrapper = ibeisAnnotationInfoMapWrapper;
    }
}
