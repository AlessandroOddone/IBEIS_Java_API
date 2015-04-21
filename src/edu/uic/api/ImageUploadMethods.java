package edu.uic.api;

import edu.uic.exceptions.BadHttpRequestException;
import edu.uic.exceptions.UnsuccessfulHttpRequestException;
import edu.uic.exceptions.UnsupportedImageFileTypeException;

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
