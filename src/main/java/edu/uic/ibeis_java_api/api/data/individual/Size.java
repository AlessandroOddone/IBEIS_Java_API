package edu.uic.ibeis_java_api.api.data.individual;

import edu.uic.ibeis_java_api.values.LengthUnitOfMeasure;

public class Size {

    private double value;
    private LengthUnitOfMeasure unitOfMeasure;

    public Size(double value, LengthUnitOfMeasure unitOfMeasure) {
        this.value = value;
        this.unitOfMeasure = unitOfMeasure;
    }

    public LengthUnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public double getValue() {
        return value;
    }
}
