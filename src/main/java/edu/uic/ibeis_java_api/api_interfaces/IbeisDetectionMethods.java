package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.values.Species;

import java.io.IOException;
import java.util.List;

public interface IbeisDetectionMethods {

    /**
     * Detect animals of the specified species in an image
     * @param ibeisImage image on Ibeis server
     * @param species species to detect
     * @return list of annotations: each annotation corresponds to an individual detected in the image
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    List<IbeisAnnotation> runAnimalDetection(IbeisImage ibeisImage, Species species)
            throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Detect animals of the specified species in multiple images
     * @param ibeisImageList list of images on the Ibeis server
     * @param species species to detect
     * @return list of lists of annotations: each element in the list corresponds to a list of all the annotations found in the corresponding image
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    List<List<IbeisAnnotation>> runAnimalDetection(List<IbeisImage> ibeisImageList, Species species)
            throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;
}
