package edu.uic.ibeis_java_api.test;

import edu.uic.ibeis_java_api.http.HttpParameter;

import java.util.List;

public interface HttpTest extends Test {

    String getCallPath();

    List<HttpParameter> getParams();
}
