package edu.uic.ibeis_java_api.identification_tools.pre_processing.dataset_reduction;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.exceptions.HandlerNotExecutedException;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfosWrapper;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class IdentificationMinSetCoverComputationHandler {

    private IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper;
    private List<IdentificationCoverSet> remainingCoverSets;
    private Collection<IbeisAnnotation> coveredAnnotations;
    private List<IbeisDbAnnotationInfo> reducedDatabase;

    public IdentificationMinSetCoverComputationHandler(IdentificationCoverSetsCollectionWrapper coverSetsCollectionWrapper) {
        this.coverSetsCollectionWrapper = coverSetsCollectionWrapper;
        this.remainingCoverSets = coverSetsCollectionWrapper.getCoverSets();
        this.coveredAnnotations = new ArrayList<>();
        this.reducedDatabase = new ArrayList<>();
    }

    public IdentificationMinSetCoverComputationHandler execute(File minSetCoverIbeisDbAnnotationInfosWrapperFile) {
        int initialAnnotationsToCover = getAnnotationsToCoverCount();
        Collections.sort(remainingCoverSets, Collections.reverseOrder());

        while (coveredAnnotations.size() < initialAnnotationsToCover && remainingCoverSets.size() > 0 &&
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
        writeMinSetCoverIbeisDbAnnotationInfosWrapperToFile(minSetCoverIbeisDbAnnotationInfosWrapperFile);
        return this;
    }

    public IbeisDbAnnotationInfosWrapper getResult() throws HandlerNotExecutedException{
        if (reducedDatabase == null) throw new HandlerNotExecutedException();
        return new IbeisDbAnnotationInfosWrapper(ThresholdType.WITHIN_DATASET, coverSetsCollectionWrapper.getTargetSpecies(), reducedDatabase);
    }

    private int getAnnotationsToCoverCount() {
        HashSet hashSet = new HashSet();
        for (IdentificationCoverSet identificationCoverSet : remainingCoverSets) {
            hashSet.addAll(identificationCoverSet.getCoveredAnnotations());
        }
        return hashSet.size();
    }

    private void writeMinSetCoverIbeisDbAnnotationInfosWrapperToFile(File file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(new IbeisDbAnnotationInfosWrapper(ThresholdType.WITHIN_DATASET, coverSetsCollectionWrapper.getTargetSpecies(), reducedDatabase).toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}