package edu.uic.ibeis_java_api.identification_tools.pre_processing.dataset_reduction;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.exceptions.HandlerNotExecutedException;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;

import java.util.*;

public class IdentificationMinSetCoverComputationHandler {

    private IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper;

    private List<IdentificationCoverSet> remainingCoverSets;
    private Collection<IbeisAnnotation> coveredAnnotations;
    private int initialDatabaseSize;

    private List<IbeisDbAnnotationInfo> reducedDatabase;

    public IdentificationMinSetCoverComputationHandler(IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper) {
        this.coverSetsCollectionWrapper = coverSetsCollectionWrapper;

        remainingCoverSets = coverSetsCollectionWrapper.getCoverSets();
        coveredAnnotations = new ArrayList<>();
        initialDatabaseSize = remainingCoverSets.size();

        reducedDatabase = new ArrayList<>();
    }

    public IdentificationMinSetCoverComputationHandler execute() {
        Collections.sort(remainingCoverSets, Collections.reverseOrder());

        while (coveredAnnotations.size() < initialDatabaseSize && remainingCoverSets.size() > 0 &&
                remainingCoverSets.get(0).getCoveredAnnotations().size() > 0) {
            IdentificationCoverSet bestCoverSet = remainingCoverSets.get(0);
            reducedDatabase.add(bestCoverSet.getDbAnnotationInfo());
            coveredAnnotations.addAll(bestCoverSet.getCoveredAnnotations());
            remainingCoverSets.remove(bestCoverSet);

            for (IdentificationCoverSet identificationCoverSet : remainingCoverSets) {
                identificationCoverSet.getCoveredAnnotations().removeAll(coveredAnnotations);
                identificationCoverSet.getCoveredAnnotations().remove(bestCoverSet.getDbAnnotationInfo().getAnnotation());
            }
            Collections.sort(remainingCoverSets, Collections.reverseOrder());
        }
        return this;
    }

    public IbeisDbAnnotationInfosWrapper getResult() throws HandlerNotExecutedException{
        if (reducedDatabase == null) throw new HandlerNotExecutedException();
        return new IbeisDbAnnotationInfosWrapper(ThresholdType.WITHIN_DATASET, coverSetsCollectionWrapper.getTargetSpecies(), reducedDatabase);
    }
}
