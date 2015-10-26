package edu.uic.ibeis_java_api.database_upload_tools.hotspotter_database_model;

import java.io.File;

public class ImageTableEntry implements Comparable<ImageTableEntry> {

    private int id;
    private File filepath;

    public ImageTableEntry(int id, File filepath) {
        this.id = id;
        this.filepath = filepath;
    }

    public int getId() {
        return id;
    }

    public File getFilepath() {
        return filepath;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImageTableEntry)) {
            return false;
        }
        if (getId() == ((ImageTableEntry) obj).getId()) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(ImageTableEntry o) {
        return Integer.compare(getId(), o.getId());
    }

}
