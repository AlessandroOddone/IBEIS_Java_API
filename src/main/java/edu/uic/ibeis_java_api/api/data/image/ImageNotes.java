package edu.uic.ibeis_java_api.api.data.image;

import edu.uic.ibeis_java_api.api.data.Notes;

public class ImageNotes extends Notes {

    private String author;
    private String location;
    private String description;

    public ImageNotes() {
    }

    public ImageNotes(String author, String location, String description) {
        this.author = author;
        this.location = location;
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static ImageNotes fromJsonString(String jsonString) {
        return gson.fromJson(jsonString, ImageNotes.class);
    }
}
