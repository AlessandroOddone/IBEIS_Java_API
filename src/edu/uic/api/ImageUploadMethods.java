package edu.uic.api;

import edu.uic.exceptions.AuthorizationHeaderException;
import edu.uic.exceptions.InvalidHttpMethodException;
import edu.uic.exceptions.UnsuccessfulHttpRequest;
import edu.uic.exceptions.UnsupportedImageFileTypeException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

public interface ImageUploadMethods {

    /**
     * Upload an image to the Ibeis server
     * @param image
     * @return gId of the uploaded image
     * @throws UnsupportedImageFileTypeException
     * @throws UnsuccessfulHttpRequest
     */
    int uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequest,
            InvalidHttpMethodException, URISyntaxException, AuthorizationHeaderException;

    /**
     * Upload a collection of images to the Ibeis server
     * @param images
     * @return list of gId's of the uploaded images
     * @throws UnsupportedImageFileTypeException
     * @throws UnsuccessfulHttpRequest
     */
    List<Integer> uploadImages(Collection<File> images) throws UnsupportedImageFileTypeException, IOException,
            UnsuccessfulHttpRequest, InvalidHttpMethodException, URISyntaxException, AuthorizationHeaderException;
}
