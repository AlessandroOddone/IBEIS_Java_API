package edu.uic.ibeis_java_api.http;

import android.org.apache.http.NameValuePair;
import android.org.apache.http.message.BasicNameValuePair;
import edu.uic.ibeis_java_api.api.image.ImageFile;
import edu.uic.ibeis_java_api.api.image.ImageZipArchive;
import edu.uic.ibeis_java_api.exceptions.EmptyListParameterException;

import java.util.List;

public class HttpParameter {
    private String name;
    private String value;
    private boolean isFile;

    public HttpParameter(String name, String value) {
        this.name = name;
        this.value = value;
        isFile = false;
    }

    public HttpParameter(String name, Number value) {
        this(name, String.valueOf(value));
    }

    public HttpParameter(String name, List<?> values) throws EmptyListParameterException {
        if (values.isEmpty()) throw new EmptyListParameterException();

        this.name = name;

        StringBuilder valString = new StringBuilder();
        for (int i=0; i<values.size(); i++) {
            if (i == values.size()-1) {
                valString.append(values.get(i));
            }
            else {
                valString.append(values.get(i) + ",");
            }
        }
        this.value = valString.toString();
        isFile = false;
    }

    public HttpParameter(String name, boolean value) {
        this.name = name;
        this.value = value ? "true" : "false";
        isFile = false;
    }

    public HttpParameter(String name, ImageFile img) {
        this.name = name;
        this.value = img.getLocalPath();
        isFile = true;
    }

    public HttpParameter(String name, ImageZipArchive imgZip) {
        this.name = name;
        this.value = imgZip.getLocalPath();
        isFile = true;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    public String encodeInUrl() {
        return this.name + "=" + this.value;
    }

    public NameValuePair toNameValuePair() {
        return new BasicNameValuePair(this.getName(), this.getValue());
    }
}
