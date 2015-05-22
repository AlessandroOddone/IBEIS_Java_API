package edu.uic.ibeis_java_api.api.data.image;

import java.io.File;

/**
 * A zip archive in which one or more images are compressed
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
