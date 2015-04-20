package edu.uic.api;

import edu.uic.exceptions.AuthorizationHeaderException;
import edu.uic.exceptions.InvalidHttpMethodException;
import edu.uic.exceptions.UnsuccessfulHttpRequest;
import edu.uic.exceptions.UnsupportedImageFileTypeException;
import edu.uic.http.*;
import edu.uic.model.ImageZipArchive;
import edu.uic.utils.FileUtils;
import edu.uic.values.CallPath;
import edu.uic.values.SupportedImageFileExtension;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Ibeis implements ImageUploadMethods {

    // Implementation of ImageUploadMethods interface

    private static final String PARAM_IMAGE_ZIP_ARCHIVE = "image_zip_archive";

    @Override
    public int uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequest, InvalidHttpMethodException, URISyntaxException, AuthorizationHeaderException{
        // check if file extension is supported by Ibeis database
        checkExtensionIsSupported(image);
        // zip the image file into an archive and get the archive name
        File zipFilePath = FileUtils.zipFile(image, generateZipFilePath(image));
        // POST request to upload the file
        Response response = new Request(RequestMethod.POST, CallPath.IMAGE.getValue(), new ParametersList().addParameter
                (new Parameter(PARAM_IMAGE_ZIP_ARCHIVE, new ImageZipArchive(zipFilePath)))).execute();
        // check if the request is successful
        if(!response.isSuccess()) {
            throw new UnsuccessfulHttpRequest();
        }
        // TODO delete zip file
        // TODO return gId
        return 0;
    }

    @Override
    public List<Integer> uploadImages(Collection<File> images) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequest{
        for(File image : images) {
            checkExtensionIsSupported(image);
        }
        // TODO zip files
        // TODO upload .zip
        // TODO throw exception if success == false
        // TODO delete zip file
        // TODO return list of gIds
        return null;
    }

    private void checkExtensionIsSupported(File image) throws UnsupportedImageFileTypeException {
        Collection<String> supportedImageFileTypes = SupportedImageFileExtension.getValuesAsStrings();

        if(!supportedImageFileTypes.contains(FileUtils.getFileExtension(image))) {
            throw new UnsupportedImageFileTypeException();
        }
    }

    private File generateZipFilePath(File unzippedFile) {
        String unzippedFilePath = unzippedFile.getPath();
        String pathToFile = unzippedFilePath.substring(0, unzippedFilePath.lastIndexOf("/"));
        return new File(pathToFile + new SimpleDateFormat("MM-dd-yyyy_hh:mm:ss_SSS").format(new Date()) + ".zip");
    }
}
