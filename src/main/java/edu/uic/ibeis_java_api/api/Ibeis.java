package edu.uic.ibeis_java_api.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import edu.uic.ibeis_java_api.api.annotation.BoundingBox;
import edu.uic.ibeis_java_api.api.image.ImageFile;
import edu.uic.ibeis_java_api.api.image.ImageZipArchive;
import edu.uic.ibeis_java_api.api_interfaces.*;
import edu.uic.ibeis_java_api.exceptions.*;
import edu.uic.ibeis_java_api.http.*;
import edu.uic.ibeis_java_api.utils.FileUtils;
import edu.uic.ibeis_java_api.values.CallPath;
import edu.uic.ibeis_java_api.values.ParamName;
import edu.uic.ibeis_java_api.values.Species;
import edu.uic.ibeis_java_api.values.SupportedImageFileType;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller class to interact with Ibeis
 */
public class Ibeis implements InsertMethods, DeleteMethods, IbeisDetectionMethods, GetMethods, IbeisQueryMethods {

    @Override
    public IbeisImage uploadImage(File image) throws UnsupportedImageFileTypeException, IOException, UnsuccessfulHttpRequestException, BadHttpRequestException {
        // check if the extension of the image file is supported by Ibeis database
        checkFileTypeIsSupported(image);

        try {
            Response response = new Request(RequestMethod.POST, CallPath.IMAGE.getValue(), new ParametersList().addParameter
                    (new Parameter(ParamName.IMAGE_FILE.getValue(), new ImageFile(image)))).execute();

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
                throw new UnsuccessfulHttpRequestException();
            }
            return new IbeisImage(response.getContent().getAsInt());

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
    }

    @Override
    public List<IbeisImage> uploadImages(List<File> images) throws UnsupportedImageFileTypeException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        return uploadImages(images, new File(generatePathToZipFile(images.get(0)))); // same folder of first image in the list
    }

    @Override
    public List<IbeisImage> uploadImages(List<File> images, File pathToTemporaryZipFile) throws UnsupportedImageFileTypeException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        // check if the extension of each image file in the collection is supported by Ibeis database
        for(File image : images) {
            checkFileTypeIsSupported(image);
        }

        // compress the image files into a zip file and get its path
        File zipFilePath;
        try {
            zipFilePath = FileUtils.zipFiles(images, new File(pathToTemporaryZipFile.getPath() + generateZipFileName()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("error zipping file");
        }

        // upload and delete zip file
        Response response = uploadAndDeleteImageZipArchive(zipFilePath);

        // check if the request has been successful
        if(response == null || !response.isSuccess()) {
            throw new UnsuccessfulHttpRequestException();
        }

        // return list of gIds of the uploaded images
        List<IbeisImage> ibeisImageList = new ArrayList<>();
        for(JsonElement jsonElement : response.getContent().getAsJsonArray()) {
            ibeisImageList.add(new IbeisImage(jsonElement.getAsLong()));
        }
        return ibeisImageList;
    }

    /**
     * Check if the type of a file is supported by Ibeis
     * @param image
     * @throws UnsupportedImageFileTypeException
     */
    private void checkFileTypeIsSupported(File image) throws UnsupportedImageFileTypeException {
        Collection<String> supportedImageFileTypes = SupportedImageFileType.getAllValuesAsStrings();

        if(!supportedImageFileTypes.contains(FileUtils.getFileExtension(image))) {
            throw new UnsupportedImageFileTypeException();
        }
    }

    /**
     * Generate the filepath to the temporary archive to be used to upload images to Ibeis server (name of the archive excluded from the path).
     * The path to the zip file is the same folder which contains the image passed as parameter.
     * @param unzippedFilePath
     * @return Path to the zip file
     */
    private String generatePathToZipFile(File unzippedFilePath) {
        String unzippedFilePathString = unzippedFilePath.getPath();
        return unzippedFilePathString.substring(0, unzippedFilePathString.lastIndexOf("/")+1);
    }

    /**
     * Generate a unique name to be used as the name of a temporary zip archive
     * @return Unique string
     */
    private String generateZipFileName() {
        return new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss_SSS").format(new Date()) + ".zip";
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
        try {
            Response response = new Request(RequestMethod.POST, CallPath.IMAGE_ZIP.getValue(), new ParametersList().addParameter
                    (new Parameter(ParamName.IMAGE_ZIP_ARCHIVE.getValue(), new ImageZipArchive(zipFilePath)))).execute();

            // delete zip file
            deleteZipFile(zipFilePath);

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
                throw new UnsuccessfulHttpRequestException();
            }
            return response;

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
    }

    private void deleteZipFile(File zipFilePath) throws IOException {
        zipFilePath.delete();
    }

    @Override
    public IbeisIndividual addIndividual(String name) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException, IndividualNameAlreadyExistsException {
        try {
            checkIndividualNameNotExists(name);

            Response response = new Request(RequestMethod.POST, CallPath.INDIVIDUALS.getValue(), new ParametersList()
                    .addParameter(new Parameter(ParamName.NAME_TEXT_LIST.getValue(), name))).execute();

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
                throw new UnsuccessfulHttpRequestException();
            }

            return new IbeisIndividual(response.getContent().getAsJsonArray().get(0).getAsLong());

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
    }

    private void checkIndividualNameNotExists(String name) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException, IndividualNameAlreadyExistsException {
        for (IbeisIndividual ibeisIndividual : getAllIndividuals()) {
            if (name.equals(ibeisIndividual.getName())) {
                throw new IndividualNameAlreadyExistsException();
            }
        }
    }

    @Override
    public IbeisAnnotation addAnnotation(IbeisImage image, BoundingBox boundingBox) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException, IndividualNameAlreadyExistsException {
        try {
            Response response = new Request(RequestMethod.POST, CallPath.ANNOTATIONS.getValue(), new ParametersList()
                    .addParameter(new Parameter(ParamName.GID_LIST.getValue(), image.getId()))
                    .addParameter(new Parameter(ParamName.BBOX_LIST.getValue(), "[" + boundingBox.toString() + "]"))).execute();

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
                throw new UnsuccessfulHttpRequestException();
            }

            return new IbeisAnnotation(response.getContent().getAsJsonArray().get(0).getAsLong());

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
    }

    @Override
    public IbeisEncounter addEncounter(String name) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException, IndividualNameAlreadyExistsException {
        try {
            checkEncounterNameNotExists(name);

            Response response = new Request(RequestMethod.POST, CallPath.ENCOUNTERS.getValue(), new ParametersList()
                    .addParameter(new Parameter(ParamName.ENC_TEXT_LIST.getValue(), name))).execute();

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
                throw new UnsuccessfulHttpRequestException();
            }

            return new IbeisEncounter(response.getContent().getAsJsonArray().get(0).getAsLong());

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
    }

    private void checkEncounterNameNotExists(String name) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException, IndividualNameAlreadyExistsException {
        for (IbeisEncounter ibeisEncounter : getAllEncounters()) {
            if (name.equals(ibeisEncounter.getName())) {
                throw new IndividualNameAlreadyExistsException();
            }
        }
    }

    @Override
    public void deleteImage(IbeisImage image) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        deleteImages(Arrays.asList(image));
    }

    @Override
    public void deleteImages(List<IbeisImage> imageList) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        List<Number> imageIds = new ArrayList<>();
        for(IbeisImage image : imageList) {
            imageIds.add(image.getId());
        }

        try {
            Response response = new Request(RequestMethod.DELETE, CallPath.IMAGE.getValue(), new ParametersList().addParameter
                    (new Parameter(ParamName.GID_LIST.getValue(), imageIds))).execute();

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
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
    }

    @Override
    public void deleteAnnotation(IbeisAnnotation annotation) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.DELETE, CallPath.ANNOTATIONS.getValue(), new ParametersList().addParameter
                    (new Parameter(ParamName.AID_LIST.getValue(), annotation.getId()))).execute();

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
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
    }

    @Override
    public void deleteIndividual(IbeisIndividual individual) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.DELETE, CallPath.INDIVIDUALS.getValue(), new ParametersList().addParameter
                    (new Parameter(ParamName.NAME_ROWID_LIST.getValue(), individual.getId()))).execute();

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
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
    }

    @Override
    public void deleteEncounter(IbeisEncounter encounter) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.DELETE, CallPath.ENCOUNTERS.getValue(), new ParametersList().addParameter
                    (new Parameter(ParamName.EID_LIST.getValue(), encounter.getId()))).execute();

            // check if the request has been successful
            if(response == null || !response.isSuccess()) {
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
    }

    @Override
    public List<IbeisAnnotation> runAnimalDetection(IbeisImage ibeisImage, Species species) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        return runAnimalDetection(Arrays.asList(ibeisImage), species).get(0);
    }

    @Override
    public List<List<IbeisAnnotation>> runAnimalDetection(List<IbeisImage> ibeisImageList, Species species) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        List<Number> imageIds = new ArrayList<>();
        for(IbeisImage image : ibeisImageList) {
            imageIds.add(image.getId());
        }

        try {
            List<List<IbeisAnnotation>> ibeisAnnotationList = new ArrayList<>();

            Response response = new Request(RequestMethod.PUT, CallPath.ANIMAL_DETECTION.getValue(), new ParametersList()
                    .addParameter(new Parameter(ParamName.SPECIES.getValue(), species.getValue()))
                    .addParameter(new Parameter(ParamName.GID_LIST.getValue(), imageIds))).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for(JsonElement elementAnnotationsJson : response.getContent().getAsJsonArray()) {
                List<IbeisAnnotation> elementAnnotations = new ArrayList<>();

                for (JsonElement annotationJson : elementAnnotationsJson.getAsJsonArray()) {
                    elementAnnotations.add(new IbeisAnnotation(annotationJson.getAsLong()));
                }
                ibeisAnnotationList.add(elementAnnotations);
            }
            return ibeisAnnotationList;

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
    }

    public IbeisQueryResult queryNoCache(IbeisAnnotation queryAnnotation, IbeisAnnotation dbAnnotation) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        return queryNoCache(Arrays.asList(queryAnnotation), Arrays.asList(dbAnnotation)).get(0);
    }

    public IbeisQueryResult queryNoCache(IbeisAnnotation queryAnnotation, List<IbeisAnnotation> dbAnnotations) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        return queryNoCache(Arrays.asList(queryAnnotation), dbAnnotations).get(0);
    }

    public List<IbeisQueryResult> queryNoCache(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation> dbAnnotations) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            List<Number> queryAnnotationIds = new ArrayList<>();
            for(IbeisAnnotation annotation : queryAnnotations) {
                queryAnnotationIds.add(annotation.getId());
            }

            List<Number> dbAnnotationIds = new ArrayList<>();
            for(IbeisAnnotation annotation : dbAnnotations) {
                dbAnnotationIds.add(annotation.getId());
            }

            Response response = new Request(RequestMethod.GET, CallPath.QUERY_CHIPS_SIMPLE_DICT.getValue(), new ParametersList()
                    .addParameter(new Parameter(ParamName.QAID_LIST.getValue(), queryAnnotationIds))
                    .addParameter(new Parameter(ParamName.DAID_LIST.getValue(), dbAnnotationIds))
                    .addParameter(new Parameter("use_cache",false))
                    .addParameter(new Parameter("use_bigcache",false))).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisQueryResult> ibeisQueryResults = new ArrayList<>();

            JsonArray responseJsonArray = response.getContent().getAsJsonArray();
            for(int i=0; i<responseJsonArray.size(); i++) {
                JsonObject jsonObject = responseJsonArray.get(i).getAsJsonObject();
                JsonArray daidList = jsonObject.get("daid_list").getAsJsonArray();
                JsonArray scoreList = new JsonArray();
                if (!jsonObject.get("score_list").isJsonNull()) {
                    scoreList = jsonObject.get("score_list").getAsJsonArray();
                } else {
                    for(int j=0; j<daidList.size(); j++) {
                        scoreList.add(JsonNull.INSTANCE);
                    }
                }
                List<IbeisQueryScore> ibeisQueryScores = new ArrayList<>();
                for(int k=0; k<daidList.size(); k++) {
                    ibeisQueryScores.add(new IbeisQueryScore(new IbeisAnnotation(daidList.get(k).getAsLong())
                            , scoreList.get(k).isJsonNull() ? 0 : scoreList.get(k).getAsDouble()));
                }
                ibeisQueryResults.add(new IbeisQueryResult(new IbeisAnnotation(jsonObject.get("qaid").getAsLong())
                        , ibeisQueryScores));
            }
            return ibeisQueryResults;

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
    }

    @Override
    public IbeisQueryResult query(IbeisAnnotation queryAnnotation, IbeisAnnotation dbAnnotation) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        return query(Arrays.asList(queryAnnotation), Arrays.asList(dbAnnotation)).get(0);
    }


    @Override
    public IbeisQueryResult query(IbeisAnnotation queryAnnotation, List<IbeisAnnotation> dbAnnotations) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        return query(Arrays.asList(queryAnnotation), dbAnnotations).get(0);
    }

    @Override
    public List<IbeisQueryResult> query(List<IbeisAnnotation> queryAnnotations, List<IbeisAnnotation> dbAnnotations) throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            List<Number> queryAnnotationIds = new ArrayList<>();
            for(IbeisAnnotation annotation : queryAnnotations) {
                queryAnnotationIds.add(annotation.getId());
            }

            List<Number> dbAnnotationIds = new ArrayList<>();
            for(IbeisAnnotation annotation : dbAnnotations) {
                dbAnnotationIds.add(annotation.getId());
            }

            Response response = new Request(RequestMethod.GET, CallPath.QUERY_CHIPS_SIMPLE_DICT.getValue(), new ParametersList()
                    .addParameter(new Parameter(ParamName.QAID_LIST.getValue(), queryAnnotationIds))
                    .addParameter(new Parameter(ParamName.DAID_LIST.getValue(), dbAnnotationIds))).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisQueryResult> ibeisQueryResults = new ArrayList<>();

            JsonArray responseJsonArray = response.getContent().getAsJsonArray();
            for(int i=0; i<responseJsonArray.size(); i++) {
                JsonObject jsonObject = responseJsonArray.get(i).getAsJsonObject();
                JsonArray daidList = jsonObject.get("daid_list").getAsJsonArray();
                JsonArray scoreList = new JsonArray();
                if (!jsonObject.get("score_list").isJsonNull()) {
                    scoreList = jsonObject.get("score_list").getAsJsonArray();
                } else {
                    for(int j=0; j<daidList.size(); j++) {
                        scoreList.add(JsonNull.INSTANCE);
                    }
                }
                List<IbeisQueryScore> ibeisQueryScores = new ArrayList<>();
                for(int k=0; k<daidList.size(); k++) {
                    ibeisQueryScores.add(new IbeisQueryScore(new IbeisAnnotation(daidList.get(k).getAsLong())
                            , scoreList.get(k).isJsonNull() ? 0 : scoreList.get(k).getAsDouble()));
                }
                ibeisQueryResults.add(new IbeisQueryResult(new IbeisAnnotation(jsonObject.get("qaid").getAsLong())
                        , ibeisQueryScores));
            }
            return ibeisQueryResults;

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
    }

    @Override
    public List<IbeisImage> getAllImages() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisImage> ibeisImages = new ArrayList<>();
            for (JsonElement imageIdJson : response.getContent().getAsJsonArray()) {
                ibeisImages.add(new IbeisImage(imageIdJson.getAsLong()));
            }
            return ibeisImages;

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
    }

    @Override
    public IbeisImage getImageById(long id) throws InvalidImageIdException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.IMAGE.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for (JsonElement imageIdJson : response.getContent().getAsJsonArray()) {
                if(imageIdJson.getAsLong() == id) return new IbeisImage(id);
            }
            throw new InvalidImageIdException();

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
    }

    @Override
    public List<IbeisIndividual> getAllIndividuals() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.INDIVIDUALS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisIndividual> ibeisIndividuals = new ArrayList<>();
            for (JsonElement nameIdJson : response.getContent().getAsJsonArray()) {
                ibeisIndividuals.add(new IbeisIndividual(nameIdJson.getAsLong()));
            }
            return ibeisIndividuals;

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
    }

    @Override
    public IbeisIndividual getIndividualById(long id) throws InvalidIndividualIdException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.INDIVIDUALS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for (JsonElement nameIdJson : response.getContent().getAsJsonArray()) {
                if(nameIdJson.getAsLong() == id) return new IbeisIndividual(id);
            }
            throw new InvalidIndividualIdException();

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

    }

    @Override
    public List<IbeisAnnotation> getAllAnnotations() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ANNOTATIONS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisAnnotation> ibeisAnnotations = new ArrayList<>();
            for (JsonElement annotationIdJson : response.getContent().getAsJsonArray()) {
                ibeisAnnotations.add(new IbeisAnnotation(annotationIdJson.getAsLong()));
            }
            return ibeisAnnotations;

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
    }

    @Override
    public IbeisAnnotation getAnnotationById(long id) throws InvalidAnnotationIdException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ANNOTATIONS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for (JsonElement annotationIdJson : response.getContent().getAsJsonArray()) {
                if(annotationIdJson.getAsLong() == id) return new IbeisAnnotation(id);
            }
            throw new InvalidAnnotationIdException();

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
    }

    @Override
    public List<IbeisEncounter> getAllEncounters() throws IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ENCOUNTERS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            List<IbeisEncounter> ibeisEncounters = new ArrayList<>();
            for (JsonElement encounterIdJson : response.getContent().getAsJsonArray()) {
                ibeisEncounters.add(new IbeisEncounter(encounterIdJson.getAsLong()));
            }
            return ibeisEncounters;

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
    }

    @Override
    public IbeisEncounter getEncounterById(long id) throws InvalidEncounterIdException, IOException, BadHttpRequestException, UnsuccessfulHttpRequestException {
        try {
            Response response = new Request(RequestMethod.GET, CallPath.ENCOUNTERS.getValue()).execute();

            if(response == null || !response.isSuccess()) {
                System.out.println("Unsuccessful Request");
                throw new UnsuccessfulHttpRequestException();
            }

            for (JsonElement encounterIdJson : response.getContent().getAsJsonArray()) {
                if(encounterIdJson.getAsLong() == id) return new IbeisEncounter(id);
            }
            throw new InvalidEncounterIdException();

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
    }
}
