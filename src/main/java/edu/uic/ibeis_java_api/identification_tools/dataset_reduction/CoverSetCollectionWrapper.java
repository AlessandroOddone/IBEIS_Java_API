package edu.uic.ibeis_java_api.identification_tools.dataset_reduction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.uic.ibeis_java_api.interfaces.JsonSerializable;

import java.util.ArrayList;
import java.util.Collection;

public class CoverSetCollectionWrapper implements JsonSerializable<CoverSetCollectionWrapper> {

    private Collection<CoverSet> coverSets = new ArrayList<>();

    public Collection<CoverSet> getCoverSets() {
        return coverSets;
    }

    public void add(CoverSet coverSet) {
        coverSets.add(coverSet);
    }

    @Override
    public String toJson() {
        return getGson().toJson(this);
    }

    public static CoverSetCollectionWrapper fromJson(String jsonString) {
        return getGson().fromJson(jsonString, CoverSetCollectionWrapper.class);
    }

    private static Gson getGson() {
        return new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
    }
}
