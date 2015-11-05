package edu.uic.ibeis_java_api.api;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisEncounter;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.api.IbeisIndividual;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface DeleteMethods {

    /**
     * Delete an image from Ibeis database
     * @param image
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    void deleteImage(IbeisImage image) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Delete a list of images from Ibeis database
     * @param imageList
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    void deleteImages(List<IbeisImage> imageList) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Delete an annotation from Ibeis database
     * @param annotation
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    void deleteAnnotation(IbeisAnnotation annotation) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Delete an individual from Ibeis database
     * @param individual
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    void deleteIndividual(IbeisIndividual individual) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Delete an encounter from Ibeis database
     * @param encounter
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    void deleteEncounter(IbeisEncounter encounter) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

}
