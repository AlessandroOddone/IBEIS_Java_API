package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.api.IbeisIndividual;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface QueryDatabaseMethods {

    /**
     * Get all the images in Ibeis database
     * @return List of IbeisImage elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisImage> getAllImages() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get all the individuals in Ibeis database
     * @return List of IbeisIndividual elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisIndividual> getAllIndividuals() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Get all the annotations in Ibeis database
     * @return List of IbeisAnnotation elements
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    public List<IbeisAnnotation> getAllAnnotations() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

}
