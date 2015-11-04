package edu.uic.ibeis_java_api.identification_tools.pre_processing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;
import edu.uic.ibeis_java_api.interfaces.JsonSerializable;
import edu.uic.ibeis_java_api.values.Species;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class for a map containing IbeisDbAnnotationInfo objects. Includes attributes with common values for all the objects in the map.
 */
public class IbeisDbAnnotationInfoMapWrapper implements JsonSerializable<IbeisDbAnnotationInfoMapWrapper> {

    private ThresholdType thresholdType;
    private Species targetSpecies;
    private Map<Long,IbeisDbAnnotationInfo> map = new HashMap<>();

    public IbeisDbAnnotationInfoMapWrapper(ThresholdType thresholdType, Species targetSpecies) {
        this.thresholdType = thresholdType;
        this.targetSpecies = targetSpecies;
    }

    public ThresholdType getThresholdType() {
        return thresholdType;
    }

    public Species getTargetSpecies() {
        return targetSpecies;
    }

    public Map<Long,IbeisDbAnnotationInfo> getMap() {
        return map;
    }

    public void add(IbeisDbAnnotationInfo ibeisDbAnnotationInfo) {
        map.put(ibeisDbAnnotationInfo.getAnnotation().getId(), ibeisDbAnnotationInfo);
    }

    public void remove(IbeisDbAnnotationInfo ibeisDbAnnotationInfo) {
        map.remove(ibeisDbAnnotationInfo.getAnnotation().getId());
    }

    @Override
    public String toJson() {
        return getGson().toJson(this);
    }

    public static IbeisDbAnnotationInfoMapWrapper fromJson(String jsonString) {
        return getGson().fromJson(jsonString, IbeisDbAnnotationInfoMapWrapper.class);
    }

    private static Gson getGson() {
        return new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
    }
}
