package edu.uic.ibeis_java_api.values;

import java.util.ArrayList;
import java.util.Collection;

public enum SupportedImageFileExtension {
    JPG("jpg"), JPEG("jpeg"), PNG("png");

    private final String value;

    SupportedImageFileExtension(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Collection<String> getValuesAsStrings() {
        Collection<String> stringValues = new ArrayList<>();

        for(SupportedImageFileExtension ext : values()) {
            stringValues.add(ext.getValue());
        }
        return stringValues;
    }
}
