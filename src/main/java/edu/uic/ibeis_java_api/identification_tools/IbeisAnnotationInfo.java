package edu.uic.ibeis_java_api.identification_tools;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisIndividual;

public class IbeisAnnotationInfo {
    private IbeisAnnotation annotation;
    private IbeisIndividual individual;
    private double isGiraffeThreshold;
    private double recognitionThreshold;

    public IbeisAnnotationInfo(IbeisAnnotation annotation, IbeisIndividual individual, double isGiraffeThreshold,
                               double recognitionThreshold) {
        this.annotation = annotation;
        this.individual = individual;
        this.isGiraffeThreshold = isGiraffeThreshold;
        this.recognitionThreshold = recognitionThreshold;
    }

    public IbeisAnnotation getAnnotation() {
        return annotation;
    }

    public IbeisIndividual getIndividual() {
        return individual;
    }

    public double getIsGiraffeThreshold() {
        return isGiraffeThreshold;
    }

    public double getRecognitionThreshold() {
        return recognitionThreshold;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IbeisAnnotationInfo) {
            if(annotation.getId() == ((IbeisAnnotationInfo) obj).getAnnotation().getId() &&
                    individual.getId() == ((IbeisAnnotationInfo) obj).getIndividual().getId() &&
                    isGiraffeThreshold == ((IbeisAnnotationInfo) obj).isGiraffeThreshold &&
                    recognitionThreshold == ((IbeisAnnotationInfo) obj).recognitionThreshold) {
                return true;
            }
        }
        return false;
    }
}