package edu.uic.ibeis_java_api.http;

import java.util.ArrayList;
import java.util.List;

public class HttpParametersList {

    private List<HttpParameter> parameters;
    private boolean containsFile;

    public HttpParametersList() {
        parameters = new ArrayList<>();
        containsFile = false;
    }

    public HttpParametersList(List<HttpParameter> parameters) {
        this.parameters = parameters;
        for(HttpParameter p : parameters) {
            if (p.isFile()) {
                containsFile = true;
                break;
            }
        }
    }

    public List<HttpParameter> getParameters() {
        return parameters;
    }

    public boolean containsFile() {
        return containsFile;
    }

    public HttpParametersList addParameter(HttpParameter parameter) {
        parameters.add(parameter);
        if(!containsFile && parameter.isFile()) {
            containsFile = true;
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder formattedParams = new StringBuilder("");
        for(int i=0; i<parameters.size(); i++) {
            formattedParams.append(parameters.get(i).getName() + "=" + parameters.get(i).getValue());
            if (i != parameters.size()-1) {
                formattedParams.append("; ");
            }
        }
        return formattedParams.toString();
    }
}
