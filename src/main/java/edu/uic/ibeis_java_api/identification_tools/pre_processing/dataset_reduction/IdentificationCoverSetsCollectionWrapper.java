package edu.uic.ibeis_java_api.identification_tools.pre_processing.dataset_reduction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.uic.ibeis_java_api.interfaces.JsonSerializable;
import edu.uic.ibeis_java_api.values.Species;

import java.util.ArrayList;
import java.util.List;

public class IdentificationCoverSetsCollectionWrapper implements JsonSerializable<IdentificationCoverSetsCollectionWrapper> {

    private Species targetSpecies;
    private List<IdentificationCoverSet> coverSets;

    public  IdentificationCoverSetsCollectionWrapper(Species targetSpecies) {
        this.targetSpecies = targetSpecies;
        this.coverSets = new ArrayList<>();
    }

    public Species getTargetSpecies() {
        return targetSpecies;
    }

    public List<IdentificationCoverSet> getCoverSets() {
        return coverSets;
    }

    public void add(IdentificationCoverSet coverSet) {
        coverSets.add(coverSet);
    }

    @Override
    public String toJson() {
        return getGson().toJson(this);
    }

    public static IdentificationCoverSetsCollectionWrapper fromJson(String jsonString) {
        return getGson().fromJson(jsonString, IdentificationCoverSetsCollectionWrapper.class);
    }

    private static Gson getGson() {
        return new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
    }
}
