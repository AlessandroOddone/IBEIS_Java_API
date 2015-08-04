package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisEncounter;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.api.IbeisIndividual;
import edu.uic.ibeis_java_api.exceptions.*;

import java.io.IOException;
import java.util.List;

public interface GetMethods {

    /**
     * Get all the images in Ibeis database
     * @return List of IbeisImage elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisImage> getAllImages() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get the image corresponding to the id passed as parameter
     * @param id
     * @return
     * @throws InvalidImageIdException the id does not correspond to a valid image
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public IbeisImage getImageById(long id) throws InvalidImageIdException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get all the individuals in Ibeis database
     * @return List of IbeisIndividual elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisIndividual> getAllIndividuals() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get the individual corresponding to the id passed as parameter
     * @param id
     * @return
     * @throws InvalidIndividualIdException the id does not correspond to a valid individual
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public IbeisIndividual getIndividualById(long id) throws InvalidIndividualIdException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get all the annotations in Ibeis database
     * @return List of IbeisAnnotation elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getAllAnnotations() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get the annotation corresponding to the id passed as parameter
     * @param id
     * @return
     * @throws InvalidAnnotationIdException the id does not correspond to a valid annotation
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public IbeisAnnotation getAnnotationById(long id) throws InvalidAnnotationIdException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get all the encounters in Ibeis database
     * @return List of IbeisEncounter elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisEncounter> getAllEncounters() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get the encounter corresponding to the id passed as parameter
     * @param id
     * @return
     * @throws InvalidEncounterIdException the id does not correspond to a valid encounter
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public IbeisEncounter getEncounterById(long id) throws InvalidEncounterIdException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

}
