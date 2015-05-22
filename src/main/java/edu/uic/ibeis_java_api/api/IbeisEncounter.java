package edu.uic.ibeis_java_api.api;

/**
 * An encounter is a collection of images (IbeisImage) on Ibeis server
 */
public class IbeisEncounter {

    private long id;

    public IbeisEncounter(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
