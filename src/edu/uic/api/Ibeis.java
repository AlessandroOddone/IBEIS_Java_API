package edu.uic.api;

import com.google.gson.JsonElement;
import edu.uic.exceptions.*;
import edu.uic.http.*;
import edu.uic.model.ImageZipArchive;
import edu.uic.utils.FileUtils;
import edu.uic.values.CallPath;
import edu.uic.values.SupportedImageFileExtension;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Ibeis implements ImageUploadMethods {

    /**
     *
     * Implementation of ImageUploadMethods interface
     *
     */

    private static final String PARAM_IMAGE_ZIP_ARCHIVE = "image_zip_archive";

    @Override
    public int uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequestException, BadHttpRequestException{
        // check if image file extension is supported by Ibeis database
        checkExtensionIsSupported(image);

        // compress the image file into a zip file and get its path
        File zipFilePath;
        try {
            zipFilePath = FileUtils.zipFile(image, generateZipFilePath(image));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("error zipping the file");
        }

        // upload zip file
        Response response = uploadImageZipArchive(zipFilePath);

        // return gId of the uploaded image
        return response.getContent().getAsInt();
    }

    @Override
    public List<Integer> uploadImages(List<File> images) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequestException, BadHttpRequestException{
        // check if extension of each image file in the collection is supported by Ibeis database
        for(File image : images) {
            checkExtensionIsSupported(image);
        }

        // compress the image files into a zip file and get its path
        File zipFilePath;
        try {
            zipFilePath = FileUtils.zipFiles(images, generateZipFilePath(images.get(0))); // same folder of first image in the list
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("error zipping the file");
        }

        // upload zip file
        Response response = uploadImageZipArchive(zipFilePath);

        // return list of gIds of the uploaded images
        List<Integer> gIds = new ArrayList<>();
        for(JsonElement jsonElement : response.getContent().getAsJsonArray()) {
            gIds.add(jsonElement.getAsInt());
        }
        return gIds;
    }

    /**
     * Check if the extension of a file is an image file extension supported by the database
     * @param image
     * @throws UnsupportedImageFileTypeException
     */
    private void checkExtensionIsSupported(File image) throws UnsupportedImageFileTypeException {
        Collection<String> supportedImageFileTypes = SupportedImageFileExtension.getValuesAsStrings();

        if(!supportedImageFileTypes.contains(FileUtils.getFileExtension(image))) {
            throw new UnsupportedImageFileTypeException();
        }
    }

    /**
     * Generates the filepath of the temporary zip file to be used to upload images to Ibeis server
     * (the zip file is created in the same folder of the image passed as parameter)
     * @param unzippedFile
     * @return Zip archive filepath
     */
    private File generateZipFilePath(File unzippedFile) {
        String unzippedFilePath = unzippedFile.getPath();
        String pathToFile = unzippedFilePath.substring(0, unzippedFilePath.lastIndexOf("/")+1);
        return new File(pathToFile + new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss_SSS").format(new Date()) + ".zip");
    }

    /**
     * Upload a zip archive of images to the server via http POST request and returns the http response
     * @param zipFilePath
     * @return Http Response
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws IOException
     */
    private Response uploadImageZipArchive(File zipFilePath) throws BadHttpRequestException, UnsuccessfulHttpRequestException, IOException {
        // http POST request to upload the zip file
        Response response;
        try {
            response = new Request(RequestMethod.POST, CallPath.IMAGE.getValue(), new ParametersList().addParameter
                    (new Parameter(PARAM_IMAGE_ZIP_ARCHIVE, new ImageZipArchive(zipFilePath)))).execute();
        } catch (AuthorizationHeaderException e) {
            deleteZipFile(zipFilePath); // delete zip file
            e.printStackTrace();
            throw new BadHttpRequestException("error in authorization header");
        } catch (URISyntaxException | MalformedURLException e) {
            deleteZipFile(zipFilePath); // delete zip file
            e.printStackTrace();
            throw new BadHttpRequestException("invalid url");
        } catch (InvalidHttpMethodException e) {
            deleteZipFile(zipFilePath); // delete zip file
            e.printStackTrace();
            throw new BadHttpRequestException("invalid http method");
        }

        // delete zip file
        deleteZipFile(zipFilePath);

        // check if the request has been successful
        if(!response.isSuccess()) {
            throw new UnsuccessfulHttpRequestException();
        }
        return response;
    }

    private void deleteZipFile(File zipFilePath) throws IOException {
        try {
            Files.deleteIfExists(zipFilePath.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("error deleting file \"" + zipFilePath + "\"");
        }
    }
}
