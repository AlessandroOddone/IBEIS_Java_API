package edu.uic.ibeis_java_api.api;

/**
 * An annotation is a specific sighting of an animal within an image
 */
public class IbeisAnnotation {

    private int id;

    protected IbeisAnnotation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
