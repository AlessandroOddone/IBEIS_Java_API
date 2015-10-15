package edu.uic.ibeis_java_api.api.individual;

import edu.uic.ibeis_java_api.api.additional_info.Notes;
import edu.uic.ibeis_java_api.values.ConservationStatus;

import java.util.Date;

public class IndividualNotes extends Notes {

    private String location;
    private String description;
    private Date dateOfBirth;
    private Weight weight;
    private Size size;
    private String habitat;
    private String diet;
    private ConservationStatus conservationStatus;
    private String other;

    public IndividualNotes() {}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public ConservationStatus getConservationStatus() {
        return conservationStatus;
    }

    public void setConservationStatus(ConservationStatus conservationStatus) {
        this.conservationStatus = conservationStatus;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public static IndividualNotes fromJsonString(String jsonString) {
        return gson.fromJson(jsonString, IndividualNotes.class);
    }
}
