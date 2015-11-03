package edu.uic.ibeis_java_api.identification_tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.uic.ibeis_java_api.identification_tools.thresholds.ThresholdType;
import edu.uic.ibeis_java_api.interfaces.JsonSerializable;

import java.util.HashMap;
import java.util.Map;

public class IbeisAnnotationInfoMapWrapper implements JsonSerializable<IbeisAnnotationInfoMapWrapper> {

    private ThresholdType thresholdType;
    private Map<Long,IbeisAnnotationInfo> map = new HashMap<>();

    public  IbeisAnnotationInfoMapWrapper(ThresholdType thresholdType) {
        this.thresholdType = thresholdType;
    }

    public ThresholdType getThresholdType() {
        return thresholdType;
    }

    public Map<Long,IbeisAnnotationInfo> getMap() {
        return map;
    }

    public void add(IbeisAnnotationInfo ibeisAnnotationInfo) {
        map.put(ibeisAnnotationInfo.getAnnotation().getId(), ibeisAnnotationInfo);
    }

    public void remove(IbeisAnnotationInfo ibeisAnnotationInfo) {
        map.remove(ibeisAnnotationInfo.getAnnotation().getId());
    }

    @Override
    public String toJson() {
        return getGson().toJson(this);
    }

    public static IbeisAnnotationInfoMapWrapper fromJsonString(String jsonString) {
        return getGson().fromJson(jsonString, IbeisAnnotationInfoMapWrapper.class);
    }

    private static Gson getGson() {
        return new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
    }
}
