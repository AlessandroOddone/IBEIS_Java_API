package edu.uic.ibeis_java_api.model;

import java.io.File;

public class ImageZipArchive {

    private String localPath;

    public ImageZipArchive(String localPath) {
        this.localPath =localPath;
    }

    public ImageZipArchive(File localPath) {
        this.localPath = localPath.toString();
    }

    public String getLocalPath() {
        return localPath;
    }
}
