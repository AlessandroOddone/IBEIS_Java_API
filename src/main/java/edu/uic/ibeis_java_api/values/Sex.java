package edu.uic.ibeis_java_api.values;

import edu.uic.ibeis_java_api.exceptions.InvalidSexException;

public enum Sex {
    MALE(1), FEMALE(0), UNKNOWN(-1);

    private int value;

    Sex(int value) {
        this.value = value;
    }

    public int getValue() {
        return  value;
    }

    public static Sex fromValue(int value) throws InvalidSexException {
        for (Sex s : Sex.values()) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new InvalidSexException();
    }

    @Override
    public String toString() {
        if (this.equals(MALE)) {
            return "Male";
        } else if (this.equals(FEMALE)) {
            return "Female";
        } else {
            return "Unknown";
        }
    }
}
