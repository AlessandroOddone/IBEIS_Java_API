package edu.uic.ibeis_java_api.api;

import edu.uic.ibeis_java_api.exceptions.EmptyListParameterException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface DeleteMethodsInterface {

    /**
     * Delete an image from Ibeis database
     * @param image
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws EmptyListParameterException
     */
    void deleteImage(IbeisImage image) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    /**
     * Delete a list of images from Ibeis database
     * @param imageList
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws EmptyListParameterException
     */
    void deleteImages(List<IbeisImage> imageList) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    /**
     * Delete an annotation from Ibeis database
     * @param annotation
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws EmptyListParameterException
     */
    void deleteAnnotation(IbeisAnnotation annotation) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    /**
     * Delete a list of annotations from Ibeis database
     * @param annotationList
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws EmptyListParameterException
     */
    void deleteAnnotations(List<IbeisAnnotation> annotationList) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    /**
     * Delete an individual from Ibeis database
     * @param individual
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws EmptyListParameterException
     */
    void deleteIndividual(IbeisIndividual individual) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    /**
     * Delete a list of individuals from Ibeis database
     * @param individualList
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws EmptyListParameterException
     */
    void deleteIndividuals(List<IbeisIndividual> individualList) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    /**
     * Delete an encounter from Ibeis database
     * @param encounter
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws EmptyListParameterException
     */
    void deleteEncounter(IbeisEncounter encounter) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    /**
     * Delete a list of encounters from Ibeis database
     * @param encounterList
     * @throws IOException
     * @throws MalformedHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws EmptyListParameterException
     */
    void deleteEncounters(List<IbeisEncounter> encounterList) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

}
