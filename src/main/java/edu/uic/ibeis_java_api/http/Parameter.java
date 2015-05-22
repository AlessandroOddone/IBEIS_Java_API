package edu.uic.ibeis_java_api.http;

import edu.uic.ibeis_java_api.api.data.image.ImageZipArchive;
import android.org.apache.http.NameValuePair;
import android.org.apache.http.message.BasicNameValuePair;

import java.util.List;

public class Parameter {
    private String name;
    private String value;
    private boolean isFile;

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
        isFile = false;
    }

    public Parameter(String name, Number value) {
        this(name, String.valueOf(value));
    }

    public Parameter(String name, List<?>values) {
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

    public Parameter(String name, ImageZipArchive imgZip) {
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
