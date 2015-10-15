package edu.uic.ibeis_java_api.api.image;

import java.io.File;

/**
 * An image file, defined by its local path
 */
public class ImageFile {

    private String localPath;

    public ImageFile(String localPath) {
        this.localPath = localPath;
    }

    public ImageFile(File localPath) {
        this.localPath = localPath.toString();
    }

    public String getLocalPath() {
        return localPath;
    }
}
