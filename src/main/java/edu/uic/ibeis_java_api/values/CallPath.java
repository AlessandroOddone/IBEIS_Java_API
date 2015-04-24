package edu.uic.ibeis_java_api.values;

public enum CallPath {

    IMAGE("/image/");

    private String value;

    CallPath(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
