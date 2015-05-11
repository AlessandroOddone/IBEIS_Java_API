package edu.uic.ibeis_java_api.values;

public enum ParamName {
    IMAGE_ZIP_ARCHIVE("image_zip_archive"),
    SPECIES("species"),
    GID_LIST("gid_list");

    private String value;

    ParamName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
