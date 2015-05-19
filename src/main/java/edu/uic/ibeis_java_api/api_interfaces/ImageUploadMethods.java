package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsupportedImageFileTypeException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageUploadMethods {

    /**
     * Upload an image to the Ibeis server.
     * This method locally creates and destroys a zip archive in the same folder where the image is located (write permissions on the folder are needed).
     * @param image
     * @return id of the uploaded image
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    IbeisImage uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Upload a collection of images to the Ibeis server (better performances than looping over list and calling UploadImage).
     * This method locally creates and destroys a zip archive in the same folder where the first image in the list is located (write permissions on the folder are needed).
     * @param images
     * @return list of id's of the uploaded images
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    List<IbeisImage> uploadImages(List<File> images) throws UnsupportedImageFileTypeException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Upload an image to the Ibeis server. This method locally creates and destroys a zip archive in the folder given in input (write permissions on the folder are needed).
     * @param image
     * @param pathToTemporaryZipFile folder in which the archive will be created (do not include filename in the path)
     * @return id of the uploaded image
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    IbeisImage uploadImage(File image, File pathToTemporaryZipFile) throws UnsupportedImageFileTypeException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Upload a collection of images to the Ibeis server (better performances than looping over list and calling UploadImage).
     * This method locally creates and destroys a zip archive in the folder given in input (write permissions on the folder are needed).
     * @param images
     * @param pathToTemporaryZipFile folder in which the archive will be created (do not include filename in the path)
     * @return list of id's of the uploaded images
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    List<IbeisImage> uploadImages(List<File> images, File pathToTemporaryZipFile) throws UnsupportedImageFileTypeException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;
}
