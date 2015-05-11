package edu.uic.ibeis_java_api.api;

/**
 * Image on Ibeis server
 */
public class IbeisImage {

    private int id;

    protected IbeisImage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
