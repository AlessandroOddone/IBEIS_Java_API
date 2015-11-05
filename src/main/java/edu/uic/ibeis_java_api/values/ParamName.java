package edu.uic.ibeis_java_api.values;

public enum ParamName {
    IMAGE_FILE("image"),
    IMAGE_ZIP_ARCHIVE("image_zip_archive"),
    SPECIES("species"),
    AID_LIST("aid_list"),
    EID_LIST("eid_list"),
    ENCOUNTER_ROWID_LIST("encounter_rowid_list"),
    GID_LIST("gid_list"),
    NID_LIST("nid_list"),
    NAME_ROWID_LIST("name_rowid_list"),
    LAT_LIST("lat_list"),
    LON_LIST("lon_list"),
    NOTES_LIST("notes_list"),
    UNIXTIME_LIST("unixtime_list"),
    NAME_SEX_LIST("name_sex_list"),
    NAME_TEXT_LIST("name_text_list"),
    ENCOUNTER_NOTES_LIST("encounter_note_list"),
    ENC_TEXT_LIST("enctext_list"),
    ENCOUNTER_TEXT_LIST("encounter_text_list"),
    QAID_LIST("qaid_list"),
    DAID_LIST("daid_list"),
    BBOX_LIST("bbox_list"),
    FRESH("fresh");

    private String value;

    ParamName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return value;
    }
}
