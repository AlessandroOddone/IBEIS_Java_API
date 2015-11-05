package edu.uic.ibeis_java_api.identification_tools.identification_algorithm.result;

import edu.uic.ibeis_java_api.api.IbeisIndividual;
import edu.uic.ibeis_java_api.values.Species;

public class IdentificationAlgorithmResult {

    private IbeisIndividual individual;
    private Species species;
    private TestingModeInfo testingModeInfo;

    public IdentificationAlgorithmResult() {

    }

    public IdentificationAlgorithmResult(Species species) {
        this.species = species;
    }

    public IdentificationAlgorithmResult(IbeisIndividual individual, Species species) {
        this.individual = individual;
        this.species = species;
    }

    public IbeisIndividual getIndividual() {
        return individual;
    }

    public void setIndividual(IbeisIndividual individual) {
        this.individual = individual;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public TestingModeInfo getTestingModeInfo() {
        return testingModeInfo;
    }

    public void setTestingModeInfo(TestingModeInfo testingModeInfo) {
        this.testingModeInfo = testingModeInfo;
    }
}
