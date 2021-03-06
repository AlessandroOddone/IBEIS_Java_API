package edu.uic.ibeis_java_api.values;

public enum LengthUnitOfMeasure {

    METER("m"), FOOT("ft");

    private String value;

    LengthUnitOfMeasure(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
