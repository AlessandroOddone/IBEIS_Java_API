package edu.uic.ibeis_java_api.api;

import edu.uic.ibeis_java_api.exceptions.EmptyListParameterException;
import edu.uic.ibeis_java_api.exceptions.MalformedHttpRequestException;
import edu.uic.ibeis_java_api.exceptions.UnsuccessfulHttpRequestException;

import java.io.IOException;
import java.util.List;

public interface IbeisQueryMethodsInterface {

    IbeisQueryResult query(IbeisAnnotation queryAnnotation, IbeisAnnotation dbAnnotation)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    IbeisQueryResult query(IbeisAnnotation queryAnnotation, List<IbeisAnnotation>dbAnnotations)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;

    List<IbeisQueryResult> query(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation>dbAnnotations)
            throws IOException, MalformedHttpRequestException, UnsuccessfulHttpRequestException, EmptyListParameterException;
}
