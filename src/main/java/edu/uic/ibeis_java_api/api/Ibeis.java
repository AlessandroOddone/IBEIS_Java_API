package edu.uic.ibeis_java_api.api;

import com.google.gson.JsonElement;
import edu.uic.ibeis_java_api.api_interfaces.DetectionMethods;
import edu.uic.ibeis_java_api.api_interfaces.ImageUploadMethods;
import edu.uic.ibeis_java_api.exceptions.*;
import edu.uic.ibeis_java_api.http.*;
import edu.uic.ibeis_java_api.utils.FileUtils;
import edu.uic.ibeis_java_api.values.CallPath;
import edu.uic.ibeis_java_api.values.ParamName;
import edu.uic.ibeis_java_api.values.Species;
import edu.uic.ibeis_java_api.values.SupportedImageFileExtension;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Ibeis implements ImageUploadMethods, DetectionMethods {

    /**
     *
     * Implementation of ImageUploadMethods interface
     *
     */

    @Override
    public IbeisImage uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequestException {
        return uploadImages(Arrays.asList(image)).get(0);
    }

    @Override
    public List<IbeisImage> uploadImages(List<File> images) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequestException {
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
            throw new IOException("error zipping file");
        }

        // upload and delete zip file
        Response response = null;
        try {
            response = uploadAndDeleteImageZipArchive(zipFilePath);
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
        }

        // return list of gIds of the uploaded images
        List<IbeisImage> ibeisImageList = new ArrayList<>();
        for(JsonElement jsonElement : response.getContent().getAsJsonArray()) {
            ibeisImageList.add(new IbeisImage(jsonElement.getAsInt()));
        }
        return ibeisImageList;
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
     * Generate the filepath of the temporary zip file to be used to upload images to Ibeis server
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
     * Upload a zip archive of images to the server via http POST request, delete the archive and return the http response
     * @param zipFilePath
     * @return Http Response
     * @throws BadHttpRequestException
     * @throws UnsuccessfulHttpRequestException
     * @throws IOException
     */
    private Response uploadAndDeleteImageZipArchive(File zipFilePath) throws BadHttpRequestException, UnsuccessfulHttpRequestException, IOException {
        // http POST request to upload the zip file
        Response response;
        try {
            response = new Request(RequestMethod.POST, CallPath.IMAGE.getValue(), new ParametersList().addParameter
                    (new Parameter(ParamName.IMAGE_ZIP_ARCHIVE.getValue(), new ImageZipArchive(zipFilePath)))).execute();
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
        zipFilePath.delete();
    }


    /**
     *
     * Implementation of DetectionMethods interface
     *
     */

    @Override
    public List<IbeisAnnotation> runAnimalDetection(IbeisImage ibeisImage, Species species) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        return runAnimalDetection(Arrays.asList(ibeisImage), species).get(0);
    }

    @Override
    public List<List<IbeisAnnotation>> runAnimalDetection(List<IbeisImage> ibeisImageList, Species species) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        List<Integer> imageIds = new ArrayList<>();
        for(IbeisImage image : ibeisImageList) {
            imageIds.add(image.getId());
        }

        System.out.println("imageIds: " + imageIds);

        List<List<IbeisAnnotation>> ibeisAnnotationList = new ArrayList<>();
        Response response;
        try {
            response = new Request(RequestMethod.PUT, CallPath.ANIMAL_DETECTION.getValue(), new ParametersList()
                    .addParameter(new Parameter(ParamName.SPECIES.getValue(), species.getValue()))
                    .addParameter(new Parameter(ParamName.GID_LIST.getValue(), imageIds))).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

        } catch (AuthorizationHeaderException e) {
            e.printStackTrace();
            throw new BadHttpRequestException("error in authorization header");
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            throw new BadHttpRequestException("invalid url");
        } catch (InvalidHttpMethodException e) {
            e.printStackTrace();
            throw new BadHttpRequestException("invalid http method");
        }

        for(JsonElement elementAnnotationsJsonElement : response.getContent().getAsJsonArray()) {
            List<IbeisAnnotation> elementAnnotations = new ArrayList<>();
            for (JsonElement annotationJsonElement : elementAnnotationsJsonElement.getAsJsonArray()) {
                elementAnnotations.add(new IbeisAnnotation(annotationJsonElement.getAsInt()));
            }
            ibeisAnnotationList.add(elementAnnotations);
        }
        return ibeisAnnotationList;
    }
}
