package edu.uic.ibeis_java_api.identification_tools.dataset_reduction;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.identification_tools.IbeisAnnotationInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CoverSet implements Comparable<CoverSet> {

    private IbeisAnnotationInfo dbAnnotationInfo;
    private List<IbeisAnnotation> coveredAnnotations = new ArrayList<>();

    public CoverSet(IbeisAnnotationInfo dbAnnotationInfo) {
        this.dbAnnotationInfo = dbAnnotationInfo;
    }

    public IbeisAnnotationInfo getDbAnnotationInfo() {
        return dbAnnotationInfo;
    }

    public List<IbeisAnnotation> getCoveredAnnotations() {
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
                ",is_giraffe_threshold:" + dbAnnotationInfo.getIsGiraffeThreshold() +
                ",rec_threshold:" + dbAnnotationInfo.getRecognitionThreshold() + "}" +
                ",covered_aids: " + "[" + coveredAnnotsStringBuilder.toString() + "]";
    }

    @Override
    public int compareTo(CoverSet o) {
        if (this.coveredAnnotations.size() == 1 && o.getCoveredAnnotations().size() == 1) {
            if (this.coveredAnnotations.get(0) == this.dbAnnotationInfo.getAnnotation() &&
                    o.getCoveredAnnotations().get(0) != o.getDbAnnotationInfo().getAnnotation()) {
                return -1;
            }
        }
        return Integer.compare(this.coveredAnnotations.size(),o.getCoveredAnnotations().size());
    }
}
