package edu.uic.ibeis_java_api.values;

public enum ConservationStatus {

    EX("Extinct"), EW("Extinct in the wild"), CR("Critically endangered"), EN("Endangered"), VU("Vulnerable"),
    NT("Near threatened"), LC("Least concern"), DD("Data deficient"), NE("Not evaluated");

    private String value;

    ConservationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return  value;
    }

}
