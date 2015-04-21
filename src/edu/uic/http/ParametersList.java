package edu.uic.http;

import java.util.ArrayList;
import java.util.List;

public class ParametersList {

    private List<Parameter> parameters;
    private boolean containsFile;

    public ParametersList() {
        parameters = new ArrayList<>();
        containsFile = false;
    }

    public ParametersList(List<Parameter> parameters) {
        this.parameters = parameters;
        for(Parameter p : parameters) {
            if (p.isFile()) {
                containsFile = true;
                break;
            }
        }
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public boolean containsFile() {
        return containsFile;
    }

    public ParametersList addParameter(Parameter parameter) {
        parameters.add(parameter);
        if(!containsFile && parameter.isFile()) {
            containsFile = true;
        }
        return this;
    }
}
