package edu.uic.ibeis_java_api.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum SupportedImageFileType {
    JPEG(Arrays.asList("jpg","jpeg","JPG","JPEG")), PNG(Arrays.asList("png","PNG"));

    private final List<String> values;

    SupportedImageFileType(String value) {
        this.values = new ArrayList<>();
        this.values.add(value);
    }

    SupportedImageFileType(List<String> values) {
        this.values = new ArrayList<>(values);
    }

    public List<String> getValues() {
        return values;
    }

    public static Collection<String> getAllValuesAsStrings() {
        Collection<String> stringValues = new ArrayList<>();

        for(SupportedImageFileType type : values()) {
            for (String typeValue : type.getValues()) {
                stringValues.add(typeValue);
            }
        }
        return stringValues;
    }
}
