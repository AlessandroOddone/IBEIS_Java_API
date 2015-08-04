package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface DeleteMethods {

    /**
     * Delete an image on Ibeis server
     * @param image
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    void deleteImage(IbeisImage image) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    /**
     * Delete a list of images on Ibeis server
     * @param imageList
     * @throws IOException
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     */
    void deleteImages(List<IbeisImage> imageList) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;
}
