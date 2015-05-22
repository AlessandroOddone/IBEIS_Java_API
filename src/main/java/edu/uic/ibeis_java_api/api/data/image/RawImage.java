package edu.uic.ibeis_java_api.api.data.image;

import edu.uic.ibeis_java_api.values.SupportedImageFileType;

/**
 * RawImage class contains the bytes that define the image and information about the image file type
 */
public class RawImage {

    private SupportedImageFileType fileType;
    private byte[] bytes;

    public RawImage(SupportedImageFileType fileType, byte[] bytes) {
        this.fileType = fileType;
        this.bytes = bytes;
    }

    public SupportedImageFileType getFileType() {
        return fileType;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
