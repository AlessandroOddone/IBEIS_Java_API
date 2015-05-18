package edu.uic.ibeis_java_api.values;

public enum ParamName {
    IMAGE_ZIP_ARCHIVE("image_zip_archive"),
    SPECIES("species"),
    AID_LIST("aid_list"),
    GID_LIST("gid_list"),
    NID_LIST("nid_list"),
    NAME_ROWID_LIST("name_rowid_list"),
    LAT_LIST("lat_list"),
    LON_LIST("lon_list"),
    NOTES_LIST("notes_list"),
    UNIXTIME_LIST("unixtime_list"),
    NAME_SEX_TEST_LIST("name_sex_text_list"),
    NAME_TEXT_LIST("name_text_list");

    private String value;

    ParamName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
