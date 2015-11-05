package edu.uic.ibeis_java_api.identification_tools;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisIndividual;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;

/**
 * Information regarding a database annotation
 */
public class IbeisDbAnnotationInfo {
    private IbeisAnnotation annotation;
    private IbeisIndividual individual;
    private Double isOfTargetSpeciesThreshold;
    private Double recognitionThreshold;
    private Boolean ofTargetSpecies;
    private Boolean outsider;

    public IbeisDbAnnotationInfo(IbeisAnnotation annotation) throws UnsuccessfulHttpRequestException, MalformedHttpRequestException, IOException {
        this.annotation = annotation;
        this.individual = annotation.getIndividual();
    }

    public IbeisAnnotation getAnnotation() {
        return annotation;
    }

    public IbeisIndividual getIndividual() {
        return individual;
    }

    public Double getIsOfTargetSpeciesThreshold() {
        return isOfTargetSpeciesThreshold;
    }

    public double getRecognitionThreshold() {
        return recognitionThreshold;
    }

    public Boolean isOfTargetSpecies() {
        return ofTargetSpecies;
    }

    public Boolean isOutsider() {
        return outsider;
    }

    public void setIsOfTargetSpeciesThreshold(Double isOfTargetSpeciesThreshold) {
        this.isOfTargetSpeciesThreshold = isOfTargetSpeciesThreshold;
    }

    public void setRecognitionThreshold(Double recognitionThreshold) {
        this.recognitionThreshold = recognitionThreshold;
    }

    public void setOfTargetSpecies(Boolean ofTargetSpecies) {
        this.ofTargetSpecies = ofTargetSpecies;
    }

    public void setOutsider(Boolean outsider) {
        this.outsider = outsider;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IbeisDbAnnotationInfo) {
            if(annotation.getId() == ((IbeisDbAnnotationInfo) obj).getAnnotation().getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(annotation.getId()).hashCode();
    }
}