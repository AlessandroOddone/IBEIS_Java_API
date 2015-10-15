package edu.uic.ibeis_java_api.api.notes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Notes class contains a Json string to hold additional information about an image, an individual or an encounter
 */
public abstract class Notes {

    protected static final Gson gson = new GsonBuilder().serializeNulls().create();

    public String toJsonString() {
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return this.toJsonString();
    }
}
