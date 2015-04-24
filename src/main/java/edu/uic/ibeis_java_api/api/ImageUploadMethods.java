package edu.uic.ibeis_java_api.api;

import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsupportedImageFileTypeException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageUploadMethods {

    /**
     * Upload an image to the Ibeis server
     * @param image
     * @return gId of the uploaded image
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     * @throws BadHttpRequestException
     */
    int uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequestException,
            BadHttpRequestException;

    /**
     * Upload a collection of images to the Ibeis server
     * @param images
     * @return list of gIds of the uploaded images
     * @throws UnsupportedImageFileTypeException
     * @throws IOException
     * @throws UnsuccessfulHttpRequestException
     * @throws BadHttpRequestException
     */
    List<Integer> uploadImages(List<File> images) throws UnsupportedImageFileTypeException, IOException,
            UnsuccessfulHttpRequestException, BadHttpRequestException;
}
