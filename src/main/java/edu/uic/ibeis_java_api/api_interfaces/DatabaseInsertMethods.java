package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.api.IbeisIndividual;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.IndividualNameAlreadyExistsException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsupportedImageFileTypeException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DatabaseInsertMethods {

    /**
     * Upload an image to the Ibeis server (Http POST).
     * @param image
     * @return id of the uploaded image
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    IbeisImage uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, BadHttpRequestException,
            UnsuccessfulHttpRequestException;

    /**
     * Upload a list of images to the Ibeis server (Http POST).
     * Better performances than looping over a big list and calling UploadImage.
     * This method locally creates and destroys a zip archive in the same folder where the first image in the list is located (write permissions on the folder are needed).
     * @param images
     * @return list of id's of the uploaded images
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    List<IbeisImage> uploadImages(List<File> images) throws UnsupportedImageFileTypeException, IOException,
            BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Upload a collection of images to the Ibeis server (Http POST).
     * Better performances than looping over a big list and calling UploadImage.
     * This method locally creates and destroys a zip archive in the folder given in input (write permissions on the folder are needed).
     * @param images
     * @param pathToTemporaryZipFile folder in which the archive will be created (do not include filename in the path)
     * @return list of id's of the uploaded images
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    List<IbeisImage> uploadImages(List<File> images, File pathToTemporaryZipFile) throws
            UnsupportedImageFileTypeException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Add a new individual to Ibeis server (Http POST)
     * @param name name of the individual to add
     * @return IbeisIndividual object corresponding to the newly created individual
     */
    IbeisIndividual addIndividual(String name) throws UnsupportedImageFileTypeException,
            IOException, BadHttpRequestException, UnsuccessfulHttpRequestException, IndividualNameAlreadyExistsException;
}
