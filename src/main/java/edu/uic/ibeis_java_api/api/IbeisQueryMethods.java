package edu.uic.ibeis_java_api.api;

import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface IbeisQueryMethods {

    IbeisQueryResult query(IbeisAnnotation queryAnnotation, IbeisAnnotation dbAnnotation)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    IbeisQueryResult query(IbeisAnnotation queryAnnotation, List<IbeisAnnotation>dbAnnotations)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;

    List<IbeisQueryResult> query(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation>dbAnnotations)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException;
}
