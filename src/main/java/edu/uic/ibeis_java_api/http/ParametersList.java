package edu.uic.ibeis_java_api.http;

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

    public static String printParamsList(ParametersList paramsList) {
        StringBuilder formattedParams = new StringBuilder("");
        for(int i=0; i<paramsList.getParameters().size(); i++) {
            formattedParams.append(paramsList.getParameters().get(i).getName() + "=" + paramsList.getParameters().get(i).getValue());
            if (i != paramsList.getParameters().size()-1) {
                formattedParams.append("; ");
            }
        }
        return formattedParams.toString();
    }

}
