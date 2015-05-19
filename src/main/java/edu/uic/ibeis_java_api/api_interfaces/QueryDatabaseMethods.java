package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisImage;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface QueryDatabaseMethods {

    public List<IbeisImage> getAllImages() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;
}
