package edu.uic.ibeis_java_api.api;

/**
 * The size of an image as its width and length
 */
public class ImageSize {
    
    private int width;
    private int length;

    public ImageSize(int width, int length) {
        this.width = width;
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return width + "x" + length;
    }
}
