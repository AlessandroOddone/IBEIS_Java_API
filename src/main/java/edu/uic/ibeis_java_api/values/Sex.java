package edu.uic.ibeis_java_api.values;

public enum Sex {
    MALE("M"), FEMALE("F"), UNKNOWN("UNKNOWN");

    private String value;

    Sex(String value) {
        this.value = value;
    }

    public String getValue() {
        return  value;
    }
}
