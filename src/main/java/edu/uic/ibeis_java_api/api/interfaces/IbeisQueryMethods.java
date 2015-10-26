package edu.uic.ibeis_java_api.api.interfaces;

import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.api.IbeisQueryResult;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface IbeisQueryMethods {

    IbeisQueryResult query(IbeisAnnotation queryAnnotation, IbeisAnnotation dbAnnotation)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    IbeisQueryResult query(IbeisAnnotation queryAnnotation, List<IbeisAnnotation>annotationDatabase)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    List<IbeisQueryResult> query(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation>annotationDatabase)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;
}
