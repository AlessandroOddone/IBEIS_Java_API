package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.values.Species;

import java.io.IOException;
import java.util.List;

public interface DetectionMethods {

    public List<IbeisAnnotation> runAnimalDetection(IbeisImage ibeisImage, Species species) throws IOException, UnsuccessfulHttpRequestException;

    public List<List<IbeisAnnotation>> runAnimalDetection(List<IbeisImage> ibeisImage, Species species) throws IOException, UnsuccessfulHttpRequestException;
}
