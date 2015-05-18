package edu.uic.ibeis_java_api.values;

public enum CallPath {

    ANIMAL_DETECTION("/core/detect_random_forest/"),
    ANNOTATION_BOUNDING_BOX("/annot/bboxes/"),
    ANNOTATION_INDIVIDUAL("/annot/name_rowids/"),
    ANNOTATION_NEIGHBORS("/annot/contact_aids/"),
    IMAGE("/image/"),
    INDIVIDUAL_ANNOTATIONS("/name/aids/"),
    INDIVIDUAL_NAME("/name/texts/"),
    INDIVIDUAL_SEX("/name/sex_text/");

    private String value;

    CallPath(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
