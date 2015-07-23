package edu.uic.ibeis_java_api.values;

public enum CallPath {

    ANIMAL_DETECTION("/core/detect_random_forest/"),
    ANNOTATIONS("/annot/"),
    ANNOTATION_BOUNDING_BOX("/annot/bboxes/"),
    ANNOTATION_INDIVIDUAL("/annot/name_rowids/"),
    ANNOTATION_NEIGHBORS("/annot/contact_aids/"),
    ENCOUNTERS("/encounter/"),
    ENCOUNTER_NAME("/encounter/text/"),
    ENCOUNTER_IMAGES("/encounter/gids/"),
    ENCOUNTER_INDIVIDUALS("/encounter/nids/"),
    ENCOUNTER_NOTE("/encounter/note/"),
    ENCOUNTER_NOTES("/encounter/notes/"),
    IMAGE("/image/"),
    IMAGE_ZIP("/image/zip/"),
    IMAGE_ANNOTATIONS("/image/aids/"),
    IMAGE_ANNOTATIONS_OF_SPECIES("/image/aids_of_species/"),
    IMAGE_GPS("/image/gps/"),
    IMAGE_NOTES("/image/notes/"),
    IMAGE_SIZE("/image/sizes/"),
    IMAGE_UNIXTIME("/image/unixtime/"),
    INDIVIDUALS("/name/"),
    INDIVIDUAL_ANNOTATIONS("/name/aids/"),
    INDIVIDUAL_IMAGES("/name/gids/"),
    INDIVIDUAL_NAME("/name/texts/"),
    INDIVIDUAL_NOTES("/name/notes/"),
    INDIVIDUAL_SEX("/name/sex/");

    private String value;

    CallPath(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
