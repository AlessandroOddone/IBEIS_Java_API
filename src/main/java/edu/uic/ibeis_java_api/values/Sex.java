package edu.uic.ibeis_java_api.values;

public enum Sex {
    MALE(1), FEMALE(0), UNKNOWN(-1);

    private int value;

    Sex(int value) {
        this.value = value;
    }

    public int getValue() {
        return  value;
    }
}
