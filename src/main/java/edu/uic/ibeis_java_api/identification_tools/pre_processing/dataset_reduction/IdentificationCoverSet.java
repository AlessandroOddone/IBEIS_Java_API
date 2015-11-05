package edu.uic.ibeis_java_api.identification_tools.pre_processing.dataset_reduction;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.identification_tools.IbeisDbAnnotationInfo;

import java.util.*;

public class IdentificationCoverSet implements Comparable<IdentificationCoverSet> {

    private IbeisDbAnnotationInfo dbAnnotationInfo;
    private Set<IbeisAnnotation> coveredAnnotations;

    public IdentificationCoverSet(IbeisDbAnnotationInfo dbAnnotationInfo) {
        this.dbAnnotationInfo = dbAnnotationInfo;
        this.coveredAnnotations = new HashSet<>();
    }

    public IbeisDbAnnotationInfo getDbAnnotationInfo() {
        return dbAnnotationInfo;
    }

    public Set<IbeisAnnotation> getCoveredAnnotations() {
        return coveredAnnotations;
    }

    public void add(IbeisAnnotation annotation) {
        this.coveredAnnotations.add(annotation);
    }

    public void remove(IbeisAnnotation annotation) {
        this.coveredAnnotations.remove(annotation);
    }

    public void remove(Collection<IbeisAnnotation> annotations) {
        this.coveredAnnotations.removeAll(annotations);
    }

    @Override
    public String toString() {
        StringBuilder coveredAnnotsStringBuilder = new StringBuilder();
        for (IbeisAnnotation annotation : coveredAnnotations) {
            coveredAnnotsStringBuilder.append(annotation.getId() + ",");
        }
        coveredAnnotsStringBuilder.deleteCharAt(coveredAnnotsStringBuilder.lastIndexOf(","));

        return "[annotation_db_element:{aid:" + dbAnnotationInfo.getAnnotation().getId() +
                ",is_of_target_species_threshold:" + dbAnnotationInfo.getIsOfTargetSpeciesThreshold() +
                ",rec_threshold:" + dbAnnotationInfo.getRecognitionThreshold() + "}" +
                ",covered_aids: " + "[" + coveredAnnotsStringBuilder.toString() + "]";
    }

    @Override
    public int compareTo(IdentificationCoverSet o) {
        return Integer.compare(this.coveredAnnotations.size(),o.getCoveredAnnotations().size());
    }
}
