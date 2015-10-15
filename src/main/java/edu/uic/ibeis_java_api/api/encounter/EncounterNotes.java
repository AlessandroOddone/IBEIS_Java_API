package edu.uic.ibeis_java_api.api.encounter;

import edu.uic.ibeis_java_api.api.additional_info.Notes;

public class EncounterNotes extends Notes {

    private String location;
    private String description;
    private String other;

    public EncounterNotes() {
    }

    public EncounterNotes(String location, String description) {
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

    public static EncounterNotes fromJsonString(String jsonString) {
        return gson.fromJson(jsonString, EncounterNotes.class);
    }
}
