package edu.uic.ibeis_java_api.identification_tools.pre_processing.dataset_reduction;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.IbeisDbAnnotationInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IdentificationMinSetCoverCalculationHandler {

    private IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper;
    private List<IbeisDbAnnotationInfo> reducedDatabase;
    private List<IdentificationCoverSet> remainingCoverSets;
    private Collection<IbeisAnnotation> coveredAnnotations;
    private int initialDatabaseSize;

    public IdentificationMinSetCoverCalculationHandler(IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper) {
        this.coverSetsCollectionWrapper = coverSetsCollectionWrapper;
        this.reducedDatabase = new ArrayList<>();
        this.remainingCoverSets = coverSetsCollectionWrapper.getCoverSets();
        this.coveredAnnotations = new ArrayList<>();
        this.initialDatabaseSize = remainingCoverSets.size();
    }

    public IdentificationMinSetCoverCalculationHandler execute() {
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

    public List<IbeisDbAnnotationInfo> getResult() {
        return reducedDatabase;
    }

}
