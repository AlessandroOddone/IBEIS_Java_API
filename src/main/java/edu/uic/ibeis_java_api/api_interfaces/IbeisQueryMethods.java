package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisQueryResult;
import edu.uic.ibeis_java_api.exceptions.BadHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface IbeisQueryMethods {

    IbeisQueryResult query(IbeisAnnotation queryAnnotation, IbeisAnnotation dbAnnotation)
            throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    IbeisQueryResult query(IbeisAnnotation queryAnnotation, List<IbeisAnnotation>annotationDatabase)
            throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;

    List<IbeisQueryResult> query(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation>annotationDatabase)
            throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException;
}
