package edu.uic.ibeis_java_api.values;

public enum WeightUnitOfMeasure {

    KILOGRAM("kg"), POUND("lbs");

    private String value;

    WeightUnitOfMeasure(String value) {
        this.value = value;
    }

    public String asString() {
        return  value;
    }
}
