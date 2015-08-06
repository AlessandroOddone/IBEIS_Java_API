package edu.uic.ibeis_java_api.api.data.individual;

import edu.uic.ibeis_java_api.values.WeightUnitOfMeasure;

public class Weight {

    private double value;
    private WeightUnitOfMeasure unitOfMeasure;

    public Weight(double value, WeightUnitOfMeasure unitOfMeasure) {
        this.value = value;
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getValue() {
        return value;
    }

    public WeightUnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }
}
