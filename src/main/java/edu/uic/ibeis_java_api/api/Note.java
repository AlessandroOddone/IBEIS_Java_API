package edu.uic.ibeis_java_api.api;

/**
 * A note contains a string of text that can be used to store additional information
 */
public class Note {

    private String text;

    public Note() {
        this.text = "";
    }

    public Note(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
