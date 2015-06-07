package edu.uic.ibeis_java_api.api.data.individual;

import edu.uic.ibeis_java_api.api.data.Notes;

public class IndividualNotes extends Notes {

    private String location;
    private String description;
    private String other;

    public IndividualNotes() {}

    public IndividualNotes(String location, String description) {
        this.location = location;
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public static IndividualNotes fromJsonString(String jsonString) {
        return gson.fromJson(jsonString, IndividualNotes.class);
    }
}
