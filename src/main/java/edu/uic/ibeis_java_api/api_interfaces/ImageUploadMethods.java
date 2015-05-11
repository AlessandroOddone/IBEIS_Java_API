package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsupportedImageFileTypeException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageUploadMethods {

    /**
     * Upload an image to the Ibeis server
     * @param image
     * @return id of the uploaded image
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    IbeisImage uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequestException;

    /**
     * Upload a collection of images to the Ibeis server (better performance than looping over list and calling UploadImage)
     * @param images
     * @return list of id's of the uploaded images
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     */
    List<IbeisImage> uploadImages(List<File> images) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequestException;
}
