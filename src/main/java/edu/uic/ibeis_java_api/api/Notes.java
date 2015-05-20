package edu.uic.ibeis_java_api.api;

/**
 * A note contains a Json string to hold additional information about an image
 */
public class Notes {

    //TODO handle JSON string

    private String jsonString;

    public Notes() {
    }

    public Notes(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    public String toString() {
        return jsonString;
    }
}
