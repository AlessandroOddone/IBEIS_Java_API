package edu.uic.ibeis_java_api.identification_tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.uic.ibeis_java_api.api.IbeisAnnotation;
import edu.uic.ibeis_java_api.identification_tools.pre_processing.thresholds_computation.ThresholdType;
import edu.uic.ibeis_java_api.interfaces.JsonSerializable;
import edu.uic.ibeis_java_api.values.Species;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for a map containing IbeisDbAnnotationInfo objects. Includes attributes with common values for all the objects in the map.
 */
public class IbeisDbAnnotationInfosWrapper implements JsonSerializable<IbeisDbAnnotationInfosWrapper> {

    private ThresholdType thresholdType;
    private Species targetSpecies;
    private Map<Long,IbeisDbAnnotationInfo> ibeisDbAnnotationInfosMap;
    private List<IbeisAnnotation> ibeisDbAnnotationList = new ArrayList<>();

    public IbeisDbAnnotationInfosWrapper(ThresholdType thresholdType, Species targetSpecies) {
        this.thresholdType = thresholdType;
        this.targetSpecies = targetSpecies;
        this.ibeisDbAnnotationInfosMap = new HashMap<>();
    }

    public IbeisDbAnnotationInfosWrapper(ThresholdType thresholdType, Species targetSpecies, Map<Long, IbeisDbAnnotationInfo> map) {
        this.thresholdType = thresholdType;
        this.targetSpecies = targetSpecies;
        this.ibeisDbAnnotationInfosMap = map;
        for (IbeisDbAnnotationInfo i : map.values()) {
            ibeisDbAnnotationList.add(i.getAnnotation());
        }
    }

    public IbeisDbAnnotationInfosWrapper(ThresholdType thresholdType, Species targetSpecies, List<IbeisDbAnnotationInfo> list) {
        this.thresholdType = thresholdType;
        this.targetSpecies = targetSpecies;
        this.ibeisDbAnnotationInfosMap = listToMap(list);
        for (IbeisDbAnnotationInfo i : list) {
            ibeisDbAnnotationList.add(i.getAnnotation());
        }
    }

    public ThresholdType getThresholdType() {
        return thresholdType;
    }

    public Species getTargetSpecies() {
        return targetSpecies;
    }

    public Map<Long, IbeisDbAnnotationInfo> getIbeisDbAnnotationInfosMap() {
        return ibeisDbAnnotationInfosMap;
    }

    public List<IbeisAnnotation> getIbeisDbAnnotationList() {
        return ibeisDbAnnotationList;
    }

    public void add(IbeisDbAnnotationInfo ibeisDbAnnotationInfo) {
        ibeisDbAnnotationInfosMap.put(ibeisDbAnnotationInfo.getAnnotation().getId(), ibeisDbAnnotationInfo);
        ibeisDbAnnotationList.add(ibeisDbAnnotationInfo.getAnnotation());
    }

    public void remove(IbeisDbAnnotationInfo ibeisDbAnnotationInfo) {
        ibeisDbAnnotationInfosMap.remove(ibeisDbAnnotationInfo.getAnnotation().getId());
        ibeisDbAnnotationList.remove(ibeisDbAnnotationInfo.getAnnotation());
    }

    @Override
    public String toJson() {
        return getGson().toJson(this);
    }

    public static IbeisDbAnnotationInfosWrapper fromJson(String jsonString) {
        return getGson().fromJson(jsonString, IbeisDbAnnotationInfosWrapper.class);
    }

    private static Gson getGson() {
        return new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
    }

    private static Map<Long,IbeisDbAnnotationInfo> listToMap(List<IbeisDbAnnotationInfo> list) {
        Map<Long,IbeisDbAnnotationInfo> map = new HashMap<>();
        for (IbeisDbAnnotationInfo i : list) {
            map.put(i.getAnnotation().getId(),i);
        }
        return map;
    }
}
