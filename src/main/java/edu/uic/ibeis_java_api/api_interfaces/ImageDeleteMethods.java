package edu.uic.ibeis_java_api.api_interfaces;

import edu.uic.ibeis_java_api.api.IbeisImage;

import java.util.List;

public interface ImageDeleteMethods {

    void deleteImage(IbeisImage image);

    void deleteImages(List<IbeisImage> imageList);
}
