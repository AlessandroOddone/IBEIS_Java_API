package edu.uic.ibeis_java_api.api;

import edu.uic.ibeis_java_api.api.annotation.BoundingBox;
import edu.uic.ibeis_java_api.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface InsertMethodsInterface {

    /**
     * Upload an image to Ibeis database (Http POST).
     * @param image
     * @return id of the uploaded image
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    IbeisImage uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, MalformedHttpRequestException,
            UnsuccessfulHttpRequestException;

    /**
     * Upload a list of images to Ibeis database (Http POST).
     * Better performances than looping over a big list and calling UploadImage.
     * This method locally creates and destroys a zip archive in the same folder where the first image in the list is located (write permissions on the folder are needed).
     * @param images
     * @return list of id's of the uploaded images
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    List<IbeisImage> uploadImages(List<File> images) throws UnsupportedImageFileTypeException, IOException,
            MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Upload a collection of images to Ibeis database (Http POST).
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
            UnsupportedImageFileTypeException, IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Add a new individual to Ibeis database (Http POST)
     * @param name name of the individual to add
     * @return IbeisIndividual object corresponding to the newly created individual
     */
    IbeisIndividual addIndividual(String name) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, IndividualNameAlreadyExistsException;

    /**
     * Add a new annotation to Ibeis database (Http POST)
     * @param image image in which the annotation is defined
     * @param boundingBox bounding box within the image
     * @return IbeisAnnotation object corresponding to the newly created annotation
     */
    IbeisAnnotation addAnnotation(IbeisImage image, BoundingBox boundingBox) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, IndividualNameAlreadyExistsException;


    /**
     * Add a new encounter to Ibeis database (Http POST)
     * @param name name of the encounter to add
     * @return IbeisEncounter object corresponding to the newly created encounter
     */
    IbeisEncounter addEncounter(String name) throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EncounterNameAlreadyExistsException;
}
