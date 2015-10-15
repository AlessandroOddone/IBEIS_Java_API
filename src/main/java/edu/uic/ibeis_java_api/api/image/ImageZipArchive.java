package edu.uic.ibeis_java_api.api.image;

import java.io.File;

/**
 * A zip archive which contains one or more images, defined by its local path
 */
public class ImageZipArchive {

    private String localPath;

    public ImageZipArchive(String localPath) {
        this.localPath = localPath;
    }

    public ImageZipArchive(File localPath) {
        this.localPath = localPath.toString();
    }

    public String getLocalPath() {
        return localPath;
    }
}
